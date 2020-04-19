package com.install.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Component {

		    private String name;
		    private List<Component> dependencies;


		    Component(String name) {
		      this.name = name;
		      dependencies = new ArrayList<>();
		    }

		    String getName() {
		      return name;
		    }

		    List<Component> getDependencies() {
		      return dependencies;
		    }


		    void addDependencies(Component dependency) {
		      this.dependencies.add(dependency);
		    }

		    @Override
		    public String toString() {
		      return  this.name;
		    }

		    @Override
		    public boolean equals(Object o) {
		      if (this == o) {
		        return true;
		      }
		      if (!(o instanceof Component)) {
		        return false;
		      }
		      Component software = (Component) o;
		      return Objects.equals(name, software.name);
		    }

		    @Override
		    public int hashCode() {
		      return Objects.hash(name);
		    }
}	
	
	

