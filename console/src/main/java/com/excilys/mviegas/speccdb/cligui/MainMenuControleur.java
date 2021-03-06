package com.excilys.mviegas.speccdb.cligui;

import com.excilys.mviegas.speccdb.controlers.IComputersListViewControler;
import com.excilys.mviegas.speccdb.controlers.IMainMenuControler;

import java.util.Scanner;

public enum MainMenuControleur implements IMainMenuControler {
	
	INSTANCE(ComputersListView.INSTANCE);
	
	private IComputersListViewControler mComputersListViewControler;

	private MainMenuConsole mMainMenuConsole = new MainMenuConsole(this);

	@SuppressWarnings("UnnecessaryEnumModifier")
	private MainMenuControleur(IComputersListViewControler pComputersListViewControler) {
		mComputersListViewControler = pComputersListViewControler;
	}

	public static final String SEPARATOR = "-------------------------------------------------------------------";
	
	public static final Scanner SCANNER = new Scanner(System.in);

	@Override
	public void launch() {
		mMainMenuConsole.display();
	}
	
	@Override
	public void quit() {
		mMainMenuConsole.close();
		
		SCANNER.close();
		
		System.exit(0);
	}

	@Override
	public void seeListComputers() {
		mComputersListViewControler.launch();
	}

	@Override
	public void addComputer() {
		ComputerUpdate.make().launch();
	}
	
	public static void main(String[] args) {
		INSTANCE.launch();
	}
}
