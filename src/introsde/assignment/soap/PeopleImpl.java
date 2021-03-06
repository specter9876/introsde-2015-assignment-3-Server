package introsde.assignment.soap;

import introsde.document.model.LifeStatus;
import introsde.document.model.Person;
import introsde.document.model.HealthMeasureHistory;
import introsde.document.model.MeasureDefinition;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.ArrayList;

import javax.jws.WebService;

//Service Implementation

@WebService(endpointInterface = "introsde.assignment.soap.People",
	serviceName="PeopleService")
public class PeopleImpl implements People {

    //Method #1:
	@Override
	public List<Person> getPeople() {
		return Person.getAll();
	}

    
     //Method #2:
	@Override
    //public Person readPerson(int id) {
	public Person readPerson(Long id) {
		System.out.println("---> Reading Person by id = "+id);
		Person p = Person.getPersonById((int)(long) id);
		return p;
	}
    
     //Method #3:
    @Override
	public Person updatePerson(Person person) {
        int id= person.getIdPerson();
        System.out.println("GLAAAAAAG :" +id);
        Person p = Person.getPersonById(id);
        p.setName(person.getName());
		Person.updatePerson(p);
		return p;
	}
    
     //Method #4:
	@Override
	public Person createPerson(Person person) {
		Person finalperson= Person.savePerson(person);
		return finalperson;
	}

    //Method #5:
	@Override
	public int deletePerson(Long id) {
    //public Long deletePerson(int id) {
		Person p = Person.getPersonById((int)(long) id);
        List<HealthMeasureHistory> list= HealthMeasureHistory.getAll();
        for(int i=0;i<list.size();i++){
            if(list.get(i).getPerson().getIdPerson()==(int)(long) id)
            {
                System.out.println("eliminom helathistory");
                HealthMeasureHistory.removeHealthMeasureHistory(list.get(i));
            }
        }
        
		if (p!=null) {
			Person.removePerson(p);
			return 0;
		} else {
			return 1;
		}
	}
     //Method #6:
    @Override
    //public  List<HealthMeasureHistory> readPersonHistory(int id,String measureType) {
	public  List<HealthMeasureHistory> readPersonHistory(Long id,String measureType) {
        
        int idPerson=(int)(long)id;
        List<HealthMeasureHistory> list= HealthMeasureHistory.getAll();
        List<HealthMeasureHistory> resultlist =new ArrayList<HealthMeasureHistory>();
        List<HealthMeasureHistory> finallist =new ArrayList<HealthMeasureHistory>();
        //System.out.println("FLAAAAG");
        
        HealthMeasureHistory healthtemp=null;
        for(int i=0;i<list.size();i++){
            healthtemp=list.get(i);
            if(healthtemp.getPerson().getIdPerson()==idPerson){
                System.out.println("Trovato inserisco nella lista di ritorno");
                resultlist.add(healthtemp);
                System.out.println("contiene: " +resultlist.size());
            }
            System.out.println("looop");
        }
        System.out.println("measuretype: "+measureType);
        healthtemp=null;
        for(int i=0;i<resultlist.size();i++){
            System.out.println("FLAAAAG");
            healthtemp=resultlist.get(i);
            System.out.println("===============================================================");
            System.out.println("IDMEASUREHISTORY:="+healthtemp.getIdMeasureHistory());
            
            MeasureDefinition measure =healthtemp.getMeasureDefinition();
            
            System.out.println("IDMEASURTYPE:="+measure.getIdMeasureDef());
            System.out.println("IDMEASURTYPE:="+measureType);
            System.out.println("IDPERSON:="+healthtemp.getPerson().getIdPerson());
            System.out.println("VALUE:="+healthtemp.getValue());
            System.out.println("===============================================================");
            if(measure.getMeasureName().equals(measureType)){
                
                System.out.println("OK inserita");
                finallist.add(healthtemp);
            }
            
        }
        
        return finallist;
        
        
      	}
    
     //Method #7:
    @Override
	public  List<MeasureDefinition> readMeasureTypes() {
		return MeasureDefinition.getAll();
	}

     //Method #8:
    @Override
    //public  HealthMeasureHistory readPersonMeasure(int id,String measureType,int mid) {
	public  HealthMeasureHistory readPersonMeasure(Long id,String measureType,Long mid) {
		List<HealthMeasureHistory> l= HealthMeasureHistory.getAll();
        List<HealthMeasureHistory> lf= new ArrayList<HealthMeasureHistory>();
        List<HealthMeasureHistory> lsf= new ArrayList<HealthMeasureHistory>();
    
        for (HealthMeasureHistory hl : l) {
            Person p=hl.getPerson();
            if (p.getIdPerson()==id){
                lf.add(hl);
                
            }
        }
        for (HealthMeasureHistory hl : lf) {
            MeasureDefinition measure=hl.getMeasureDefinition();
            if (measure.getMeasureName().equals(measureType)){
                lsf.add(hl);
                
            }
        }
        for (HealthMeasureHistory hl : lsf) {
            if (hl.getIdMeasureHistory()==mid){
                return hl;
            }
        }
        return null;
        //mettere controlli se vuota

        
	}
     //Method #9:
    @Override
    //public  LifeStatus savePersonMeasure(int id,LifeStatus m) {
	public  LifeStatus savePersonMeasure(Long id,LifeStatus m) {
        Person person=  Person.getPersonById((int)(long) id);
        List<LifeStatus> lifestatus= person.getLifeStatus();
        System.out.println("SAVE LOFESTATUS");
        System.out.println("mm: "+lifestatus.isEmpty()+" size "+lifestatus.size());
        if(!lifestatus.isEmpty()){
            System.out.println("already lifestatus");
            MeasureDefinition measure=null;
            MeasureDefinition measure2=null;
            //m.setPerson(person);
        
            for(LifeStatus lf : lifestatus){
                measure=lf.getMeasureDefinition();
                measure2=m.getMeasureDefinition();
                //m.setPerson(person);
                
                if(person.getIdPerson()==id){//redundant
                    System.out.println("quiii");
                    if(measure.getMeasureName().equals(measure2.getMeasureName())){
                        LifeStatus.removeLifeStatus(lf);
                    
                        HealthMeasureHistory hlm =new HealthMeasureHistory();
                        //hlm.setTimestamp(""+System.currentTimeMillis()));
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                    
                    
                        hlm.setTimestamp(dateFormat.format(date));
                        hlm.setValue(m.getValue());
                        hlm.setPerson(person);
                        hlm.setMeasureDefinition(measure);
                        //hlm.setIdMeasureDefinition(measure.getIdMeasureDef());
                        hlm.setIdMeasureDefinition(measure.getIdMeasureDef());
                        HealthMeasureHistory.saveHealthMeasureHistory(hlm);
                        lf.setValue(m.getValue());
                        LifeStatus.saveLifeStatus(lf);
                        m=lf;
                        System.out.println("salavato tutto");
                    
                    }
                }
            
               
            }
            return m;
        }
        else{
            System.out.println("no other lifestatus");
             LifeStatus lftosave1=new LifeStatus();
             MeasureDefinition mdef1=new MeasureDefinition();
             mdef1.setMeasureName("weight");
             mdef1.setIdMeasureDef(1);
             mdef1.setMeasureType("double");
             lftosave1.setMeasureDefinition(mdef1);
             lftosave1.setValue("85");
             lftosave1.setPerson(person);
             List<LifeStatus> listlf=new ArrayList <LifeStatus>();
             listlf.add(lftosave1);
            
             person.setLifeStatus(listlf);
            
            HealthMeasureHistory hlm =new HealthMeasureHistory();
            //hlm.setTimestamp(""+System.currentTimeMillis()));
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            
            
            hlm.setTimestamp(dateFormat.format(date));
            hlm.setValue(lftosave1.getValue());
            hlm.setPerson(person);
            hlm.setMeasureDefinition(mdef1);
            //hlm.setIdMeasureDefinition(measure.getIdMeasureDef());
            hlm.setIdMeasureDefinition(mdef1.getIdMeasureDef());
            HealthMeasureHistory.saveHealthMeasureHistory(hlm);
            
            System.out.println("salavato tutto");
            
            return LifeStatus.saveLifeStatus(lftosave1);
            
        }
        
       
        
	}
     //Method #10:
    @Override
    //public HealthMeasureHistory updateHealthMeasureHistory(int id, HealthMeasureHistory m){
    public HealthMeasureHistory updateHealthMeasureHistory( Long id, HealthMeasureHistory m){
        List <HealthMeasureHistory> hl=new ArrayList<HealthMeasureHistory>();
        hl=HealthMeasureHistory.getAll();
        Person p=null;
        
        for(HealthMeasureHistory hm : hl){
            p=hm.getPerson();
            if(p.getIdPerson()==id){
                if(hm.getIdMeasureHistory()==m.getIdMeasureHistory()){
                    hm.setValue(m.getValue());
                    HealthMeasureHistory.updateHealthMeasureHistory(hm);
                    return hm;
               }
            }
            
        }
        return null;
        
    }

    
   

}
