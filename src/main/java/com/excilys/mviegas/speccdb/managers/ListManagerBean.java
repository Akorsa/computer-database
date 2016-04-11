package com.excilys.mviegas.speccdb.managers;

import com.excilys.mviegas.speccdb.concurrency.ThreadLocals;
import com.excilys.mviegas.speccdb.data.Computer;
import com.excilys.mviegas.speccdb.exceptions.ConnectionException;
import com.excilys.mviegas.speccdb.persistence.ICrudService;
import com.excilys.mviegas.speccdb.persistence.Paginator;
import com.excilys.mviegas.speccdb.persistence.QueryParameter;
import com.excilys.mviegas.speccdb.persistence.jdbc.ComputerDao;
import com.excilys.mviegas.speccdb.persistence.jdbc.ComputerDao.Order;
import com.excilys.mviegas.speccdb.persistence.jdbc.ComputerDao.TypeOrder;
import com.excilys.mviegas.speccdb.persistence.jdbc.DatabaseManager;
import com.excilys.mviegas.speccdb.ui.webapp.Message;
import com.excilys.mviegas.speccdb.ui.webapp.Message.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

/**
 * Bean permettant de gérer une liste de Computeurs
 *
 * @author Mickael
 */
@ManagedBean
public class ListManagerBean {

	//===========================================================
	// Attributes static
	//===========================================================

	public static final Logger LOGGER = LoggerFactory.getLogger(ListManagerBean.class);
	
	public static final int DEFAULT_SIZE_PAGE = 10;

	//===========================================================
	// Attributes - privates
	//===========================================================
	
	private int mPage = 1;
	
	private int mSize = DEFAULT_SIZE_PAGE;
	
	private String mSearch;
	
	private Paginator<Computer> mPaginator;
	
	private ICrudService<Computer> mComputerDao;
	
	private String mOrder;
	
	private String mTypeOrder;
	
	private List<Message> mMessages = new LinkedList<>();
	
	//===========================================================
	// Constructeur
	//===========================================================

	public ListManagerBean() {
		init();
	}
	
	//===========================================================
	// Callback
	//===========================================================
	@PostConstruct
	public void init() {
		mPage = 1;
		mSize = DEFAULT_SIZE_PAGE;
		mComputerDao = ComputerDao.INSTANCE;
	}
	
	//===========================================================
	// Méthodes controleurs
	//===========================================================

	public int getPage() {
		return mPage;
	}

	public void setPage(int pPage) {
		mPage = pPage;
//		mDisplayedComputers = mComputerDao.findAll(mPage*mSize, mSize);
	}

	public int getSize() {
		return mSize;
	}

	public void setSize(int pSize) {
		mSize = pSize;
//		mDisplayedComputers = mComputerDao.findAll(mPage*mSize, mSize);
	}
	
	public String getSearch() {
		return mSearch;
	}

	public void setSearch(String pSearch) {
		mSearch = pSearch;
	}

	public Paginator<Computer> getPaginator() {
		return mPaginator;
	}

	public String getOrder() {
		return mOrder;
	}

	public void setOrder(String pOrder) {
		mOrder = pOrder;
	}

	public String getTypeOrder() {
		return mTypeOrder;
	}

	public void setTypeOrder(String pTypeOrder) {
		mTypeOrder = pTypeOrder;
	}

	//===========================================================
	// Méthodes - Object
	//===========================================================	

	@Override
	public String toString() {
		//noinspection StringBufferReplaceableByString
		StringBuilder builder = new StringBuilder();
		builder.append("ListManagerBean [mPage=");
		builder.append(mPage);
		builder.append(", mSize=");
		builder.append(mSize);
		builder.append(", mSearch=");
		builder.append(mSearch);
		builder.append(", mPaginator=");
		builder.append(mPaginator);
		builder.append(", mComputerDao=");
		builder.append(mComputerDao);
		builder.append(", mOrder=");
		builder.append(mOrder);
		builder.append(", mTypeOrder=");
		builder.append(mTypeOrder);
		builder.append(", mMessages=");
		builder.append(mMessages);
		builder.append("]");
		return builder.toString();
	}

	//===========================================================
	// Méthodes - Object
	//===========================================================
	public void update() {
		if (mPage == 0) {
			mPage = 1;
		}
		
		Connection connection;
		
		try {
			connection = DatabaseManager.getConnection();
			ThreadLocals.CONNECTIONS.set(connection);
		} catch (ConnectionException e) {
			mMessages.add(new Message("Internal Error", "We got an internal Error .\nPlease, retry later.", Level.ERROR));
			return; 
		}

		if ((mSearch != null && !mSearch.isEmpty()) || (mOrder != null && !mOrder.isEmpty())) {
			ThreadLocals.CONNECTIONS.set(connection);
			QueryParameter parameter = QueryParameter.with(ComputerDao.Parameters.FILTER_NAME, mSearch);
			parameter
					.and(ComputerDao.Parameters.SIZE, mSize)
					.and(ComputerDao.Parameters.START, (mPage - 1)*mSize)
					.and(ComputerDao.Parameters.ORDER, Order.from(mOrder))
					.and(ComputerDao.Parameters.TYPE_ORDER, TypeOrder.from(mTypeOrder))
			;

			try {
				mPaginator = mComputerDao.findWithNamedQueryWithPaginator(ComputerDao.NamedQueries.SEARCH, parameter.parameters());
			} catch (com.excilys.mviegas.speccdb.exceptions.DAOException pE) {
				LOGGER.error(pE.getMessage(), pE);
				mMessages.add(new Message("Internal Error", "We have an Eror with the Database.\nRetrieve later", Level.ERROR));
			}
		} else {
			try {
				mPaginator = mComputerDao.findAllWithPaginator((mPage-1)*mSize, mSize);
			} catch (com.excilys.mviegas.speccdb.exceptions.DAOException pE) {
				LOGGER.error(pE.getMessage(), pE);
				mMessages.add(new Message("Internal Error", "We have an Eror with the Database.\nRetrieve later", Level.ERROR));
			}
		}
		
		mMessages.add(new Message("Successful Update", "Successful Update", Level.SUCCESS));
		
		try {
			DatabaseManager.releaseConnection(connection);
		} catch (ConnectionException e) {
			LOGGER.error(e.getMessage(), e);
			mMessages.add(new Message("Internal Error", "We have an Eror with the Database.\nRetrieve later", Level.ERROR));
		}
	}

	public boolean delete(String pIntegers) {
		String[] indexes = pIntegers.split(",");
		
		boolean result = true;
		
		Connection connection;
		
		try {
			connection = DatabaseManager.getConnection();
			ThreadLocals.CONNECTIONS.set(connection);
		} catch (ConnectionException e) {
			mMessages.add(new Message("Internal Error", "We have an Eror with the Database.\nPlease, retry later.", Level.ERROR));
			return false; 
		}
		

		// TODO à optimiser
		for (String index : indexes) {
			int i = Integer.parseInt(index);
			try {
				if (!mComputerDao.delete(i)) {
					mMessages.add(new Message("Internal Error", "We have an Eror with the Database when we tried to delete the computer n°"+index+".\nWould you retry later.", Level.ERROR));
					result = false;
				}
			} catch (com.excilys.mviegas.speccdb.exceptions.DAOException pE) {
				LOGGER.error(pE.getMessage(), pE);
				mMessages.add(new Message("Internal Error", "We have an Eror with the Database when we tried to delete the computer n°"+index+".\nWould you retry later.", Level.ERROR));
				result = false;
			}
		}
		
		try {
			DatabaseManager.releaseConnection(connection);
		} catch (ConnectionException e) {
			LOGGER.error(e.getMessage(), e);
			result = false;
		}
		
		mMessages.add(new Message("Successful Deletion", "All selectede computers are deleted from the Database.", Level.SUCCESS));
		return result;
	}
}
