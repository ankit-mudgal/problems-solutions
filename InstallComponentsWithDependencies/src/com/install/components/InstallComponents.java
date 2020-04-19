package com.install.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstallComponents {


  private static Map<String, Component> allComponents = new HashMap<>();

  private static List<Component> installedComponents = new ArrayList<>();


  static void fireCommands(String[] inputCommands) {

    for (String inpCommand : inputCommands) {
      System.out.println(inpCommand);

      final String[] commandTokens = inpCommand.split("\\s+");

      final String commandString = commandTokens[0];
      final Command command = Command.valueOf(commandString);

      switch (command) {
        case DEPEND:
          final String compName = commandTokens[1];
          addDependentComponents(compName, commandTokens);
          break;
        case INSTALL:
          final Component compToBeInstalled = getComponent(commandTokens[1]);
          if (isAlreadyInstalled(compToBeInstalled)) {
            System.out.println(compToBeInstalled + " is already installed");
          } else {
            //Install dependencies first
            final List<Component> compDependenciesToBeInstalled = compToBeInstalled.getDependencies();
            for (Component compDependency : compDependenciesToBeInstalled) {
              if (!isAlreadyInstalled(compDependency)) {
                install(compDependency);
              }
            }

            //Once the dependencies are installed, install the software
            install(compToBeInstalled);
          }
          break;
        case REMOVE:
          final Component compToBeRemoved = getComponent(commandTokens[1]);
          if (!isAlreadyInstalled(compToBeRemoved)) {
            System.out.println(compToBeRemoved + " is not installed");
          }
          else if (canRemoveComponent(compToBeRemoved)) {
            System.out.println("Removing " + compToBeRemoved);
            installedComponents.remove(compToBeRemoved);

            final List<Component> currCompDependencies = compToBeRemoved.getDependencies();
            for (Component dependency : currCompDependencies) {
              if (canRemoveComponent(dependency)) {
                System.out.println("Removing " + dependency);
                installedComponents.remove(dependency);
              }
            }
          } else {
            System.out.println(compToBeRemoved + " is still needed");
          }
          break;
        case LIST:
          for (Component installedComp : installedComponents) {
            System.out.println(installedComp);
          }
          break;
        case END:
          break;
      }
    }

  }

  private static void install(Component component) {
    System.out.println("Installing " + component);
    installedComponents.add(component);
  }

  private static boolean isAlreadyInstalled(Component softwareToBeInstalled) {
    return installedComponents.contains(softwareToBeInstalled);
  }

  private static void addDependentComponents(String compName, String[] commandTokens) {
    //The dependencies of the current command are available from 3rd position onwards
    for (int i = 2; i < commandTokens.length; i++) {
      final String currentDependency = commandTokens[i];
      final List<Component> dependenciesOfdependency = getComponent(currentDependency).getDependencies();

      if (dependenciesOfdependency != null && dependenciesOfdependency.contains(getComponent(compName))) {
        
    	  System.out.println(
    	            currentDependency + " depends on " + compName + ", ignoring command");
        
      } else {
        getComponent(compName).addDependencies(getComponent(currentDependency));
      }

    }
  }

  private static Component getComponent(String name) {
    Component compon = allComponents.get(name);
    if (compon == null) {
      compon = new Component(name);
      allComponents.put(name, compon);
    }
    return compon;
  }

  private static boolean canRemoveComponent(Component compToBeRemoved) {
    for (Component installedComponent : installedComponents) {
      final List<Component> requiredDependencies = installedComponent.getDependencies();
      if (requiredDependencies != null) {
        for (Component dependency : requiredDependencies) {
          if (compToBeRemoved.equals(dependency)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  public static void main(String[] args){
    
	  String[] inputCommands = new String[]{"DEPEND TELNET TCPIP NETCARD",
        "DEPEND TCPIP NETCARD",
        "DEPEND DNS TCPIP NETCARD",
        "DEPEND BROWSER TCPIP HTML",
        "INSTALL NETCARD",
        "INSTALL TELNET",
        "INSTALL foo",
        "REMOVE NETCARD",
        "INSTALL BROWSER",
        "INSTALL DNS",
        "LIST",
        "REMOVE TELNET",
        "REMOVE NETCARD",
        "REMOVE DNS",
        "REMOVE NETCARD",
        "INSTALL NETCARD",
        "REMOVE TCPIP",
        "REMOVE BROWSER",
        "REMOVE TCPIP",
        "END"};

    fireCommands(inputCommands);
  }
}





