#############################################################################
#!/bin/sh
# Subject : Workflow of combing ontology modularity and forgetting.
# Author : Jieying Chen
# Date   : 12-08-2019
# Email  : jieying.chenchen@gmail.com
##############################################################################

cdir=`pwd`

ONTOLOGY="$1"
SIGFILE="$2"
DEPTH="$3"

echo "#############################################################################"
echo "# Subject : Workflow of combing ontology modularity and forgetting."
echo "# Author : Jieying Chen"
echo "##############################################################################"

#SIGFILE=${SIGDIR}${file}

EXDSIG=${SIGFILE}'_extendedSig.owl'
MODULE=${EXDSIG}'_starModule.owl'
FAMEVIEW=${MODULE}'-fame-view-0608.owl'
LETHEVIEW=${MODULE}'-lethe-view-v2.owl'
DEFAMEVIEW=${FAMEVIEW}'_denormalised.owl'
DELETHEVIEW=${LETHEVIEW}'_denormalised.owl'

echo "-----start to extend signature----------------------------------"
echo "-----Author: Jieying Chen---------------------------------------"

java -jar SignatureExtensionYong.jar ${SIGFILE} ${ONTOLOGY} ${DEPTH}

echo "-----start to extract star module-------------------------------"
echo "-----Author: Jieying Chen, by using OWLAPI----------------------"

java -jar ExtractStarMod.jar ${EXDSIG} ${ONTOLOGY}


echo "-----start to compute uniform interpolants----------------------"

echo "-----FAME-------------------------------------------------------"
echo "-----Author: Yizheng Zhao, packaged by Ghadah Alghamdi----------"

java -jar FAME_06082019.jar ${EXDSIG} ${MODULE}

echo "-----LETHE------------------------------------------------------"
echo "-----Author: Patrick Koopmann, packaged by Ghadah Alghamdi------"

java -jar LETHE_v2_3007.jar ${EXDSIG} ${MODULE}

echo "-----start to denormalise the results---------------------------"
echo "-----Author: Ghadah Alghamdi------------------------------------"

java -jar Denormalise.jar ${FAMEVIEW} ${FAMEVIEW}
java -jar Denormalise.jar ${LETHEVIEW} ${LETHEVIEW}


echo "-----start to add annotation------------------------------------"
echo "-----Author: Ghadah Alghamdi------------------------------------"

java -jar Add_annotations.jar ${ONTOLOGY} ${EXDSIG} ${EXDSIG}
java -jar Add_annotations.jar ${ONTOLOGY} ${DEFAMEVIEW} ${DEFAMEVIEW}
java -jar Add_annotations.jar ${ONTOLOGY} ${DELETHEVIEW} ${DELETHEVIEW}









