package com.dwl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwl.resources.ProductResource;
import com.google.inject.Guice;
import com.google.inject.Injector;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class App extends Application<GlobalMartConfiguration>{

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	@Override
	public void initialize(Bootstrap<GlobalMartConfiguration> b) {
	}

	@Override
	public void run(GlobalMartConfiguration c, Environment e)
			throws Exception {
		LOGGER.info("Method App#run() called");
		System.out.println("ProductService Using DropWizard!");
		System.out.println("Coucbase Bucket : " + c.getCouchbaseBucket());
		Injector injector = Guice.createInjector();
		e.jersey().register(new ProductResource());
	}

	public static void main(String[] args) throws Exception {
		new App().run(args);
	}

}
