package com.excilys.mviegas.speccdb.persist.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.excilys.mviegas.speccdb.data.Company;
import com.excilys.mviegas.speccdb.data.Computer;
import com.excilys.mviegas.speccdb.exceptions.DAOException;
import com.excilys.mviegas.speccdb.persist.CrudService;
import com.excilys.mviegas.speccdb.wrappers.ComputerJdbcWrapper;

public class ComputerDao implements CrudService<Computer> {
//	INSTANCE;
	
	public static final Logger LOGGER = Logger.getLogger(ComputerDao.class);
	
	public static final ComputerDao INSTANCE = new ComputerDao();

	public static final int BASE_SIZE_PAGE = 100;
	
	
	
	
	// ===========================================================
	// Attributres - private
	// ===========================================================
	private final Connection mConnection;
	
	private final PreparedStatement mCreateStatement;
	private final PreparedStatement mUpdateStatement;
	private final PreparedStatement mDeleteStatement;
	private final PreparedStatement mFindStatement;

	// ===========================================================
	// Constructors
	// ===========================================================
	private ComputerDao() {
		LOGGER.info("Init of ComputerDao");
		try {
			mConnection = DatabaseManager.getConnection();
			mCreateStatement = mConnection.prepareStatement("INSERT INTO `computer` (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			mUpdateStatement = mConnection.prepareStatement("UPDATE `computer` SET name=?, introduced=?, discontinued=?, company_id=? WHERE id = ?;");
			mDeleteStatement = mConnection.prepareStatement("DELETE FROM `computer` WHERE id = ?");
			mFindStatement = mConnection.prepareStatement("SELECT * FROM `computer` WHERE id = ?");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//===========================================================
	// Methods - private
	//===========================================================
	private void prepareStatement(Computer pT, PreparedStatement pPreparedStatement) throws SQLException {
		
		if (pT == null) {
			throw new IllegalArgumentException("computer must not be null");
		}
		pPreparedStatement.setString(1, pT.getName());
		if (pT.getIntroducedDate() != null) {
			pPreparedStatement.setDate(2, new Date(pT.getIntroducedDate().getTime()));
		} else {
			pPreparedStatement.setDate(2, null);
		}
		if (pT.getDiscontinuedDate() != null) {
			pPreparedStatement.setDate(3, new Date(pT.getDiscontinuedDate().getTime()));
		} else {
			pPreparedStatement.setDate(3, null);
		}
		if (pT.getManufacturer() != null) {
			pPreparedStatement.setInt(4, pT.getManufacturer().getId());
		} else {
			pPreparedStatement.setNull(4, Types.BIGINT);
		}
	}

	// ===========================================================
	// Methods - CrudService
	// ===========================================================

	@Override
	public Computer create(Computer pT) {
		
		if (pT == null) {
			return null;
		}
		try {
//			mCreateStatement.clearParameters();
			
			prepareStatement(pT, mCreateStatement);
			
			int nbResult = mCreateStatement.executeUpdate();
			if (nbResult == 1) {
				ResultSet a = mCreateStatement.getGeneratedKeys();
				a.next();
				pT.setId(a.getInt(1));
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Persist of "+pT);
				}
				
				return pT;
				
			}
			
			LOGGER.error("Error on persist");
			return null;
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException(e);
		}
	}

	@Override
	public Computer find(long pId) {
		try {
			mFindStatement.setLong(1, pId);
			ResultSet resultSet = mFindStatement.executeQuery();
			
			if (!resultSet.isBeforeFirst()) {
				return null;
			}
			resultSet.next();
			return ComputerJdbcWrapper.fromJdbc(resultSet);
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException(e);
		}
	}

	@Override
	public Computer update(Computer pT) {
		if (pT == null || pT.getId() <= 0) {
			throw new IllegalArgumentException("Null or Not Persisted Object");
		}
		
		try {
			prepareStatement(pT, mUpdateStatement);
			mUpdateStatement.setInt(5, pT.getId());
			int nbResult = mUpdateStatement.executeUpdate();
			if (nbResult == 1) {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("Update of "+pT.getId()+" successfull");
				}
				
				return pT;
			}
			LOGGER.error("Error on update");
			return null;
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException(e);
		}
	}

	@Override
	public boolean delete(long pId) {
		if (pId <= 0) {
			throw new IllegalArgumentException("Null or Not Persisted Object");
		}
		
		try {
			mDeleteStatement.setLong(1, pId);
			int nbLines = mDeleteStatement.executeUpdate();
			
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Delete of "+pId+(nbLines == 1 ? " successfull" : " failed"));
			}
			return nbLines == 1;
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException(e);
		}
	}
	
	

	@Override
	public boolean delete(Computer pT) {
		if (pT == null || pT.getId() <= 0) {
			throw new IllegalArgumentException("Null or Not Persisted Object");
		}
		
		try {
			mDeleteStatement.setLong(1, pT.getId());
			int nbLines = mDeleteStatement.executeUpdate();
			
			LOGGER.info("Delete of "+pT.getId()+(nbLines == 1 ? " successfull" : " failed"));
			return nbLines == 1;
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException(e);
		}
	}

	@Override
	public boolean refresh(Computer pT) {
		if (pT == null || pT.getId() <= 0) {
			throw new IllegalArgumentException("Null or Not Persisted Object");
		}
		
		Computer computer = INSTANCE.find(pT.getId());
		
		if (computer == null) {
			return false;
		}
		
		pT.setName(computer.getName());
		pT.setIntroducedDate(computer.getIntroducedDate());
		pT.setDiscontinuedDate(computer.getDiscontinuedDate());
		pT.setManufacturer(computer.getManufacturer());
		
		return true;
	}

	@Override
	public List<Computer> findAll() {
		return findAll(0, BASE_SIZE_PAGE);
	}

	@Override
	public List<Computer> findAll(int pStart, int pSize) {
		Statement statement;
		try {
			statement = mConnection.createStatement();
			if (pSize > 0) {
				statement.setMaxRows(pSize);
			} else {
				statement.setMaxRows(0);
				pSize = 0;
			}
			
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM computer LIMIT " + pSize + " OFFSET "+pStart);
			List<Computer> mCompanies = new ArrayList<>(resultSet.getFetchSize());
			
			while (resultSet.next()) {
				Computer computer = ComputerJdbcWrapper.fromJdbc(resultSet);
				mCompanies.add(computer);
			}
			return mCompanies;
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException(e);
		}
	}

	@Override
	public List<Computer> findWithNamedQuery(String pQueryName) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("ComputerDao#findWithNamedQuery not implemented yet.");
	}

	@Override
	public List<Computer> findWithNamedQuery(String pQueryName, int pResultLimit) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("ComputerDao#findWithNamedQuery not implemented yet.");
	}

	@Override
	public List<Computer> findWithNamedQuery(String pNamedQueryName, Map<String, Object> pParameters) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("ComputerDao#findWithNamedQuery not implemented yet.");
	}

	@Override
	public List<Computer> findWithNamedQuery(String pNamedQueryName, Map<String, Object> pParameters,
			int pResultLimit) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("ComputerDao#findWithNamedQuery not implemented yet.");
	}
	
	@Override
	public int size() {
		Statement statement;
		try {
			statement = mConnection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM computer");
			
			if (!resultSet.isBeforeFirst()) {
				throw new DAOException();
			}
			
			
			
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLException e) {
			LOGGER.error(e);
			throw new DAOException(e);
		}
	}

}