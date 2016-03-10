package com.excilys.mviegas.speccdb.ui.cligui;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.excilys.mviegas.speccdb.data.Company;
import com.excilys.mviegas.speccdb.data.Computer;
import com.excilys.mviegas.speccdb.persist.jdbc.CompanyDao;
import com.excilys.mviegas.speccdb.persist.jdbc.ComputerDao;

public class ComputerUpdate implements IComputerUpdateControler {

	private Computer mComputer;

	private ComputerUpdate(Computer pComputer) {
		mComputer = pComputer;
	}

	/* (non-Javadoc)
	 * @see com.excilys.mviegas.speccdb.ui.cligui.IComputerUpdateControler#launch()
	 */
	@Override
	public void launch() {
		if (mComputer == null) {
			MainMenuConsole.printBread("Création d'ordinateur");
			System.out.println("(Aide : la valeur entre parathèse est la valeur par défaut (saisissez vide)");
		} else {
			MainMenuConsole.printBread("Mise à jour de l'ordinateur " + mComputer.getId());
			System.out.println("(Aide : la valeur entre parathèse est l'ancienne (saisissez vide)");
		}

		String name = null;

		// Siasie nouveau nom
		do {
			if (mComputer == null) {
				System.out.print("> Nouveau nom : ");
			} else {
				System.out.printf("> Nouveau nom (ancien : %s) : ", mComputer.getName());
			}

			name = MainMenuControleur.SCANNER.nextLine();

			if (mComputer != null && name != null && name.equals("")) {
				name = mComputer.getName();
			} else if (name.isEmpty()) {
				System.err.println("Saisie d'un nom obligatoire !");
			}

		} while (name == null || name.equals(""));

		// saisie date introduction
		Date dateIntroduced = null;
		final String formatDate = "yyyy/MM/dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatDate);

		do {
			if (mComputer == null || mComputer.getIntroducedDate() == null) {
				System.out.print("> Date Lancement (N/A) : ");
			} else {
				System.out.printf("> Date Lancement (ancien : %s) : ", mComputer.getIntroducedDate().toString());
			}

			String t = MainMenuControleur.SCANNER.nextLine();

			if (!t.isEmpty()) {
				try {
					dateIntroduced = simpleDateFormat.parse(t);
				} catch (ParseException e) {
					System.err.println("Veuillez saisir une date dans le bon format (AAAA/MM/JJ)");
					continue;
				}
			}

			if (mComputer != null && t.isEmpty()) {
				dateIntroduced = mComputer.getIntroducedDate();
			}
			break;

		} while (true);

		// Saisie date fin
		Date dateFinished = null;

		do {
			if (mComputer == null || mComputer.getDiscontinuedDate() == null) {
				System.out.print("> Fin Production (N/A) : ");
			} else {
				System.out.printf("> Fin Production (ancien : %s) : ", mComputer.getDiscontinuedDate().toString());
			}

			String t = MainMenuControleur.SCANNER.nextLine();

			if (!t.isEmpty()) {
				try {
					dateFinished = simpleDateFormat.parse(t);
				} catch (ParseException e) {
					System.err.println("Veuillez saisir une date dans le bon format (AAAA/MM/JJ)");
					continue;
				}
			}

			if (mComputer != null && t.isEmpty()) {
				dateFinished = mComputer.getDiscontinuedDate();
			}
			break;

		} while (true);

		// Siasie fabriquant
		Company company = null;

		do {
			if (mComputer == null) {
				System.out.print("> ID du fabricant (N/A) : ");
			} else {
				if (mComputer.getManufacturer() == null) {
					System.out.printf("> ID du fabricant (ancien : (N/A) ) : ",
							mComputer.getDiscontinuedDate().toString());
				} else {
					System.out.printf("> ID du fabricant (ancien : %d (%s) ) : ", mComputer.getManufacturer().getId(),
							mComputer.getManufacturer().getName());
				}
			}

			String t = MainMenuControleur.SCANNER.nextLine();

			if (t.isEmpty()) {
				if (mComputer != null) {
					company = mComputer.getManufacturer();
				}
			} else {
				int id = -1;
				try {
					id = Integer.valueOf(t);
				} catch (NumberFormatException e) {
					System.err.println("Saisissez un ID valide");
					continue;
				}

				company = CompanyDao.INSTANCE.find(id);

				if (company == null) {
					System.err.println("Saisissez un ID valide");
					continue;
				}
			}
			break;

		} while (true);

		if (mComputer == null) {
			mComputer = new Computer(name, dateIntroduced, dateFinished, company);

			ComputerDao.INSTANCE.create(mComputer);
		} else {
			mComputer.setName(name);
			mComputer.setIntroducedDate(dateIntroduced);

			ComputerDao.INSTANCE.update(mComputer);
		}
		
		valid();

		
	}
	
	

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("ComputerUpdate#cancel not implemented yet.");
	}

	@Override
	public void valid() {
		System.out.printf("[!] Ordinateur %s (ID:%d) bien enregistrées%n", mComputer.getName(), mComputer.getId());
	}

	public static IComputerUpdateControler make(int pId) {
		Computer computer = ComputerDao.INSTANCE.find(pId);

		if (computer == null) {
			// throw new IllegalArgumentException();
			return null;
		} else {
			return new ComputerUpdate(computer);
		}
	}

	public static IComputerUpdateControler make(Computer pComputer) {
		if (pComputer == null) {
			// throw new IllegalArgumentException();
			return null;
		}

		pComputer = ComputerDao.INSTANCE.find(pComputer.getId());
		if (pComputer == null) {
			return null;
		}

		return new ComputerUpdate(pComputer);
	}

	public static IComputerUpdateControler make() {
		return new ComputerUpdate(null);
	}

}