package useCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.CrownService;
import services.ProjectService;
import utilities.AbstractTest;
import domain.Crown;

@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProjectFavTest extends AbstractTest {

	/*
	 * Listar mis proyectos favoritos - Crown
	 *
	 * -El orden de los par�metros es: Usuario (Manager) que se va a autenticar, Error esperado
	 * 
	 * Cobertura del test:
	 * -El usuario autenticado existe(test positivo)
	 * -El usuario no est� autenticado(test negativo)
	 */
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private CrownService crownService;

	
	private List<Crown> crowns;
	
	@Before
    public void setup() {
		this.crowns= new ArrayList<Crown>();
		this.crowns.addAll(this.crownService.findAll());
		
		Collections.shuffle(this.crowns);
	}
	@Test
	public void driver() {
		final Object testingData[][] = {
				{this.crowns.get(0).getUserAccount().getUsername(), null },
				{"", IllegalArgumentException.class}
				};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0],
					(Class<?>) testingData[i][1]);
	}

	protected void template(String username, final Class<?> expected) {
		Class<?> caught;
		caught = null;
		try {
			this.authenticate(username);
			
			projectService.findMyFavs();
			
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
