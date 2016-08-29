package org.ird.unfepi.service;

import java.io.Serializable;

import org.ird.unfepi.model.CenterProgram;
import org.ird.unfepi.model.HealthProgram;
import org.ird.unfepi.model.Round;

public interface HealthProgramService {
	
	Serializable saveHealthProgram(HealthProgram healthProgram);
	Serializable saveCenterProgram(CenterProgram centerProgram);
	Serializable saveRound(Round round);
}
