package org.ird.unfepi.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vaccinationcentervaccineday")
public class VaccinationCenterVaccineDay {

	@Id
	private VaccinationCenterVaccineDayId id;

/*	@ManyToOne(fetch = FetchType.EAGER, targetEntity = VaccinationCenter.class)
	@JoinColumn(name = "vaccinationCenterId")
	private VaccinationCenter vaccinationCenter;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Vaccine.class)
	@JoinColumn(name = "vaccineId")
	private Vaccine vaccine;
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = CalendarDay.class)
	@JoinColumn(name = "dayNumber")
	private CalendarDay calenderDay;*/
	
	public VaccinationCenterVaccineDay() {
		// TODO Auto-generated constructor stub
	}
	public VaccinationCenterVaccineDayId getId() {
		return id;
	}

	public void setId(VaccinationCenterVaccineDayId id) {
		this.id = id;
	}
/*	public VaccinationCenter getVaccinationCenter() {
		return vaccinationCenter;
	}

	public void setVaccinationCenter(VaccinationCenter vaccinationCenter) {
		this.vaccinationCenter = vaccinationCenter;
	}

	public Vaccine getVaccine() {
		return vaccine;
	}

	public void setVaccine(Vaccine vaccine) {
		this.vaccine = vaccine;
	}

	public CalendarDay getCalenderDay() {
		return calenderDay;
	}

	public void setCalenderDay(CalendarDay calenderDay) {
		this.calenderDay = calenderDay;
	}*/
}
