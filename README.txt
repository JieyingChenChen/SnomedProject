#############################################################################
#!/bin/sh
# Subject: Workflow of combing ontology modularity and forgetting.
# Author : Jieying Chen
# Date   : 12-08-2019
# Email  : jieying.chenchen@gmail.com
# Copyright: The University of Manchester, IHTSDO
# The author is funded by an EPSRC funded IAA Project with IHTSDO.
# People involved: Ghadah Alghamdi, Renate Schmidt and Yongsheng Gao.
##############################################################################


-	Workflow

		Input:  SNOMED CT, Signature

		Procedures:
		- Signature extension (Author: Jieying Chen)
		- Extract star module (OWLAPI, the jar file was created by Jieying Chen)
		- Forgetting: FAME  (Author: Yizheng Zhao, the jar file was created by Ghadah Alghamdi)
              		   or LETHE (Author: Patrick Koopmann, the jar file was created by Ghadah Alghamdi)
		- Denormalization (Author: Ghadah Alghamdi)
		- Add annotation  (Author: Ghadah Alghamdi)


-	For computing uniform interpolates of ONTOLOGY w.r.t. signature, one can run the script by using the command,

	./workflow.sh ${ONTOLOGYFILE} ${SIGNATUREFILE} ${DEPTH}

	For example, ./workflow.sh snomedct_intl_201901.owl  sig.owl  1

	Depth is the parameter in the algorithm that is used in the algorithm of signature extension (cf. “alg-sig-ext.png”).
	

- 	The file "SigExtentionYong.java" is the source code of "alg-sig-ext.png". The author uses "owlapi-osgidistribution-4.1.4.jar".








