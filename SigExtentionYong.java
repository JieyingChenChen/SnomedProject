/***********************************************************
 *  Copyright (C) 2019 - 2021                              *
 *  Jieying Chen (jieying.chenchen@gmail.com)              *
 *  The University of Manchester                           *
 *                                                         *
 *  This program is free software; you can redistribute    *
 *  it and/or modify it under the terms of the GNU         *
 *  General Public License as published by the Free        *
 *  Software Foundation; either version 3 of the License,  *
 *  or (at your option) any later version.                 *
 *                                                         *
 *  This program is distributed in the hope that it will   *
 *  be useful, but WITHOUT ANY WARRANTY; without even      *
 *  the implied warranty of MERCHANTABILITY or FITNESS     *
 *  FOR A PARTICULAR PURPOSE.  See the GNU General Public  *
 *  License for more details.                              *
 *                                                         *
 *  You should have received a copy of the GNU General     *
 *  Public License along with this program; if not, see    *
 *  <http://www.gnu.org/licenses/>.                        *
 ***********************************************************/


//add OWLAPI version 4.0.1

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.temporal.ValueRange;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;



public class SigExtentionYong {
	
	public static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	public static OWLDataFactory datafactory = manager.getOWLDataFactory();
	
	public static Set<OWLClass> sC = new HashSet<>();
	public static Set<OWLObjectProperty> sR= new HashSet<>();
	
	public static Set<OWLClass> ESC = new HashSet<>();
	public static Set<OWLObjectProperty> ESR= new HashSet<>();
	
	public static Set<OWLClass> lList= new HashSet<>();
	private static HashMap<OWLClass, Set<OWLClass> > cMap = new HashMap<>();
	private static HashMap<OWLClass, Set<OWLObjectProperty> > rMap = new HashMap<>();
	public static void main(String[] args) throws Exception{
		
		String ontologyFile = "", outputFile = "", moduleType = "", sigFile = "", outputSigFile = "";
		int n=1;
		
		System.out.println("==================================================================================");
	    System.out.println("                        Signature Extention");
	    System.out.println("       Author: Jieying Chen, The University of Manchester ");
	    System.out.println("==================================================================================");
	    System.out.println("");
	    System.out.println("");
		sigFile = args[0];
		ontologyFile = args[1];
		n = Integer.parseInt(args[2]);
		OWLOntologyManager sigManager = OWLManager.createOWLOntologyManager();
		OWLOntology ont = null;
	    File file = new File(sigFile);
	    Set<OWLOntology> ss = sigManager.loadOntologyFromOntologyDocument(file).getImportsClosure();
	    ont = sigManager.createOntology();
	    for(OWLOntology ont2 : ss) {
	    	sigManager.addAxioms(ont, ont2.getAxioms());
	    }
	    sC = ont.getClassesInSignature();
	    sR=ont.getObjectPropertiesInSignature();
		outputSigFile = sigFile+"_extendedSig.owl";
		OWLOntology PO = sigManager.createOntology();
	    File file1 = new File(ontologyFile);
	    Set<OWLOntology> ontologySet = manager.loadOntologyFromOntologyDocument(file1).getImportsClosure();
	    for(OWLOntology ont3 : ontologySet) {
	      manager.addAxioms(PO, ont3.getAxioms());
	    }
		Set<OWLEntity> p = new HashSet<OWLEntity>();
		p.addAll(sC);
		p.addAll(ESR);
		Set<OWLEntity> sSs = new HashSet<OWLEntity>(p);
		for(OWLAxiom a:PO.getLogicalAxioms()) {
			OWLClassExpression ce = zAx((OWLLogicalAxiom) a);
			if(ce instanceof OWLClass) {
				OWLClass aclass = ce.asOWLClass();
				if(sSs.contains(aclass)) {
					p.addAll(a.getSignature());
				}
			}
		}
		OWLOntology onto = EM(p, PO);
		Set<OWLLogicalAxiom> axioms = onto.getLogicalAxioms();
		for(OWLClass a: onto.getClassesInSignature()) {
			if(!onto.getEquivalentClassesAxioms(a).isEmpty()) {
				lList.add(a);
			}
		}
	    for(OWLClass a: onto.getClassesInSignature()) {
			gaiM(a,gFC(a, onto));
			urMap(a, fuCR(a, onto));
		}    
	    
	    Boolean ff = true;
		int num =0;
		while(ff) {
			num++;
			ff = false;
			for(OWLClass a: CC(cMap.keySet(),onto)) {
				Set<OWLClass> Gv = CC(cMap.get(a),onto);
				Set<OWLClass> v = cMap.get(a);
				Set<OWLObjectProperty> vv = rMap.get(a);
				if(v.size()>0) {
					for(OWLClass b:Gv) {
						if(lList.contains(b)) {
							ff = true;
							v.remove(b);
							v.addAll(gFC(b, onto));
							gaiM(b, gFC(b, onto));
							gaiM(a,v);
							
							vv.addAll(fuCR(b, onto));
							urMap(b, fuCR(b, onto));
							urMap(a, vv);

						}
					}					
				}
				
			}
		}
		ESC.addAll(sC);
		ESR.addAll(sR);
		for(OWLClass a: sC) {
			if(lList.contains(a)) {
				ESC.addAll(cMap.get(a));
				ESR.addAll(rMap.get(a));
			}
		}
		for(OWLLogicalAxiom ax: getFZA(onto)) {
			OWLClassExpression un = YAx(ax);
			if(un.getClassesInSignature().size()==1) {
				OWLClass unC = un.asOWLClass();
				if(ESC.contains(unC)) {
					OWLClassExpression czz = zAx(ax);
					
					for(OWLClass aclass: czz.getClassesInSignature()) {
						ESR.addAll(czz.getObjectPropertiesInSignature());
						if(lList.contains(aclass)) {
							ESC.addAll(cMap.get(aclass));
							ESR.addAll(rMap.get(aclass));
						}
						else {
							ESC.add(aclass);
						}
						
					}
					ESR.addAll(czz.getObjectPropertiesInSignature());
				}		
			}
		}
		System.out.println("extSigConcepts: "+ESC);
		while(n>0) {
			Set<OWLClass> fff = ccc(ESC);
		    Set<OWLLogicalAxiom> sa = jdAx(onto);
            n=n-1;
			for(OWLLogicalAxiom aaaa:sa) {
				OWLClassExpression zz = zAx(aaaa);
				if(zz.getSignature().size()==1) {
					OWLClass czz = zz.asOWLClass();
					if(fff.contains(czz)) {
						OWLClassExpression un = YAx(aaaa);
						ESR.addAll(un.getObjectPropertiesInSignature());
						for(OWLClass ac: un.getClassesInSignature()) {
							if(lList.contains(ac)) {
								System.out.println("extSigConcepts: "+ESC);
								ESC.addAll(cMap.get(ac));
								ESR.addAll(rMap.get(ac));
								ESC.remove(ac);
								System.out.println("extSigConcepts: "+ESC);
								System.out.println("----");
							}
							else {
								ESC.add(ac);
							}
						}						
					}
				}
				
			}
			
		}
		Set<OWLObjectProperty> sSSs= new HashSet<OWLObjectProperty>(ESR);
		for(OWLObjectProperty r: sSSs) {
			
			for(OWLSubObjectPropertyOfAxiom rAx: onto.getObjectSubPropertyAxiomsForSubProperty(r)) {

				ESR.addAll(rAx.getObjectPropertiesInSignature());
			}
		}
		OWLOntology sigOnto = manager.createOntology();
		for(OWLClass a: ESC) {
			OWLAxiom ax = datafactory.getOWLDeclarationAxiom(a);
			manager.addAxiom(sigOnto, ax);
		}
		for(OWLObjectProperty a: ESR) {
			OWLAxiom ax = datafactory.getOWLDeclarationAxiom(a);
			manager.addAxiom(sigOnto, ax);
		}
	    try {
			manager.saveOntology(sigOnto, new RDFXMLOntologyFormat(), new FileOutputStream(new File(outputSigFile)));
		} catch (OWLOntologyStorageException e) {
			System.out.println("Save signature ontology failed");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Save signature ontology as output file failed");
			e.printStackTrace();
		}
	    System.out.println("Save extended signature in: "+outputSigFile);
	    

	    
	}

	public static OWLOntology EM(Set<OWLEntity> signature, OWLOntology ontology) throws OWLOntologyCreationException, FileNotFoundException, OWLOntologyStorageException, CloneNotSupportedException {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			SyntacticLocalityModuleExtractor sme = new SyntacticLocalityModuleExtractor(manager,
					ontology, uk.ac.manchester.cs.owlapi.modularity.ModuleType.STAR);
			OWLOntology moduleOnto = sme.extractAsOntology(signature, IRI.generateDocumentIRI());
			return moduleOnto;
	}

	private static Set<OWLClass> ccc(Set<OWLClass> aa) {
		Set<OWLClass> toReturn = new HashSet<OWLClass>();
		for(OWLClass a:aa) {
			toReturn.add(a);
		}
		return toReturn;
	}

	private static Set<OWLLogicalAxiom> jdAx(OWLOntology onto) {
		Set<OWLLogicalAxiom> t = new HashSet<OWLLogicalAxiom>();
	   
	    for(OWLLogicalAxiom ax: onto.getLogicalAxioms()) {
	    	if(ax instanceof OWLSubClassOfAxiom) {
	    		t.add(ax);
	    	}
	    }
	    t.removeAll(getFZA(onto));
		return t;
	}


	private static Set<OWLLogicalAxiom> getFZA(OWLOntology onto) {
	    Set<OWLLogicalAxiom> toReturn = new HashSet<OWLLogicalAxiom>();
		for(OWLLogicalAxiom ax: onto.getLogicalAxioms()) {
			if(ax instanceof OWLSubClassOfAxiom) {
				OWLClassExpression cz = zAx(ax);
				//System.out.println(ax);
				if(cz.getClassesInSignature().size()>1) {
					toReturn.add(ax);
				}
			}		
		}
		return toReturn;	
	}

private static Set<OWLClass> CC(Set<OWLClass> input,OWLOntology onto) {
	Set<OWLClass> T = new HashSet<OWLClass>();
	for(OWLClass a: input) {
		for(OWLClass b: onto.getClassesInSignature()) {
			if(a.toString().equalsIgnoreCase(b.toString())) {
				T.add(b);
			}
		}
	}
	return T;
}

	private static void gaiM(OWLClass a, Set<OWLClass> aS) {
		if(cMap.keySet().contains(a)) {
			Set<OWLClass> value = cMap.get(a);
			value.addAll(aS);
			cMap.put(a, value);
		}
		else {
			cMap.put(a, aS);
		}
	}
	
	private static void urMap(OWLClass a, Set<OWLObjectProperty> aa) {
		if(rMap.keySet().contains(a)) {
			Set<OWLObjectProperty> value = rMap.get(a);
			value.addAll(aa);
			rMap.put(a, value);
		}
		else {
			rMap.put(a, aa);
		}
	}

	private static Set<OWLClass> gFC(OWLClass a, OWLOntology onto) {
		
		Set<OWLClass> t = new HashSet<>();
		for(OWLEquivalentClassesAxiom ax: onto.getEquivalentClassesAxioms(a)) {
			OWLClassExpression ce = YAx(ax);

			t.addAll(ce.getClassesInSignature());
			
			t.remove(a);
		}
		return t;
	}
	
	private static Set<OWLObjectProperty> fuCR(OWLClass a, OWLOntology onto) {
		
		
		Set<OWLObjectProperty> t = new HashSet<>();

		for(OWLEquivalentClassesAxiom ax: onto.getEquivalentClassesAxioms(a)) {
			OWLClassExpression ce = YAx(ax);

			t.addAll(ce.getObjectPropertiesInSignature());
			
		}
		return t;
	}



	private static OWLClassExpression zAx(OWLLogicalAxiom axiom) {
		
		OWLClassExpression zz = null;
		
		if(axiom.isOfType(AxiomType.EQUIVALENT_CLASSES))
		{
			OWLEquivalentClassesAxiom a = (OWLEquivalentClassesAxiom)axiom.getAxiomWithoutAnnotations();
			zz = a.getClassExpressionsAsList().get(0);
		}
		
		if(axiom.isOfType(AxiomType.SUBCLASS_OF))
		{
			OWLSubClassOfAxiom a = (OWLSubClassOfAxiom)axiom.getAxiomWithoutAnnotations();
			zz = a.getSubClass();
		}
		return zz;
		
	}
	
	private static OWLClassExpression YAx(OWLLogicalAxiom axiom) {
		
		OWLClassExpression toReturn = null;
		
		if(axiom.isOfType(AxiomType.EQUIVALENT_CLASSES))
		{
			OWLEquivalentClassesAxiom a = (OWLEquivalentClassesAxiom)axiom.getAxiomWithoutAnnotations();
			toReturn = a.getClassExpressionsAsList().get(1);
		}
		
		if(axiom.isOfType(AxiomType.SUBCLASS_OF))
		{
			OWLSubClassOfAxiom a = (OWLSubClassOfAxiom)axiom.getAxiomWithoutAnnotations();
			toReturn = a.getSuperClass();
		}
		return toReturn;
	}

		
}
