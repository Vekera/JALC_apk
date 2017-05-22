package cz.broforce42.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.broforce42.model.ISchedule;

/**
 * Created by vekera on 28/11/2016.
 */
public class ScheduleTest {

	private ISchedule schedule;

	@Before
	public void setUp() throws Exception {
		// Tady to je trochu problem. Kdyby byla chyba addInstallment, problem
		// se projevi
		// v dalsich metodach.
		this.schedule = new Schedule(0.0);
		this.schedule.addInstallment(new Schedule.Installment(0, 0, 500));
		this.schedule.addInstallment(new Schedule.Installment(646.39, 0));
		this.schedule.addInstallment(new Schedule.Installment(1491.67, 6333.33));
	}

	@After
	public void tearDown() throws Exception {
		this.schedule = null;
	}

	@Test
	public void getInstallments() throws Exception {
		assertEquals(3, this.schedule.getInstallments().size());
	}

	@Test
	public void addInstallment() throws Exception {
		ISchedule schedule = new Schedule(0.0);
		schedule.addInstallment(new Schedule.Installment(0, 0, 500));
		schedule.addInstallment(new Schedule.Installment(646.39, 0));

		assertEquals(2, schedule.getInstallments().size());
	}

	@Test
	public void getTotalTaxes() throws Exception {
		assertEquals(500, this.schedule.getTotalTaxes(), 0.0);
	}

	@Test
	public void getTotalPayments() throws Exception {
		assertEquals(8971.39, this.schedule.getTotalPayments(), 0.0);
	}

	@Test
	public void getStart() throws Exception {

		assertNotNull(this.schedule.getStart());
	}

	@Test
	public void getEnd() throws Exception {
		assertNotNull(this.schedule.getEnd());
	}
}