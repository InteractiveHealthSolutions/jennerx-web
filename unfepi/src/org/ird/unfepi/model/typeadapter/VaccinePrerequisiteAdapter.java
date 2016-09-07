package org.ird.unfepi.model.typeadapter;

import java.io.IOException;

import org.ird.unfepi.model.VaccinePrerequisite;
import org.ird.unfepi.model.VaccinePrerequisiteId;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class VaccinePrerequisiteAdapter extends TypeAdapter<VaccinePrerequisite>{
	
	@Override
	public VaccinePrerequisite read(JsonReader in) throws IOException {
		
		final VaccinePrerequisite vaccinePrerequisite = new VaccinePrerequisite();
		VaccinePrerequisiteId vaccinePrerequisiteId = new VaccinePrerequisiteId();
		
		in.beginObject();
		while (in.hasNext()) {
			
			String name = in.nextName();
			
			if(name.equals("vaccineId")){
				vaccinePrerequisiteId.setVaccineId(new Integer(in.nextInt()).shortValue());
			}
			else if(name.equals("vaccinePrerequisiteId")){
				vaccinePrerequisiteId.setVaccinePrerequisiteId(new Integer(in.nextInt()).shortValue());
			}
			else if(name.equals("mandatory")){
				vaccinePrerequisite.setMandatory(in.nextBoolean());
			}
			vaccinePrerequisite.setVaccinePrerequisiteId(vaccinePrerequisiteId);
		}
		in.endObject();
		
		return vaccinePrerequisite;
	}

	@Override
	public void write(JsonWriter out, VaccinePrerequisite vaccinePrerequisite) throws IOException {
	    out.beginObject();
	    out.name("vaccineId").value(vaccinePrerequisite.getVaccinePrerequisiteId().getVaccineId());
	    out.name("vaccinePrerequisiteId").value(vaccinePrerequisite.getVaccinePrerequisiteId().getVaccinePrerequisiteId());
	    out.name("mandatory").value(vaccinePrerequisite.getMandatory());
	    out.endObject();
	}


}
