#!/bin/sh
#
#$ -S /bin/sh
#$ -wd /vol/grid-solar/sgeusers/fengyali/GP
#
# End of the setup directives
##
if [ -d /local/tmp/fengyali/$JOB_ID ]; then
        cd /local/tmp/fengyali/$JOB_ID
else
        echo "Uh oh ! There's no job directory to change into "
        echo "Something is broken. I should inform the programmers"
        echo "Save some information that may be of use to them"
        echo "Here's LOCAL TMP "
        ls -la /local/tmp
        echo "AND LOCAL TMP fengyali"
        ls -la /local/tmp/fengyali
        echo "Exiting"
        exit 1
fi
#
# My local definines
DIR_GRID="/vol/grid-solar/sgeusers/fengyali/GP/"
DIR_OUTPUT=$DIR_GRID$2 # Name of directory containing output
#
cp /vol/grid-solar/sgeusers/fengyali/GP/GP.jar .
cp /vol/grid-solar/sgeusers/fengyali/GP/*.params .
mkdir config
cp -a /vol/grid-solar/sgeusers/fengyali/GP/config/* ./config


java -jar GP.jar  -classpath "." ec.Evolve -file  tutorial4.params   -p stat.file=$1.stat

# Make sure the output directory exists before trying to copy data there
mkdir -p $DIR_OUTPUT

cp *.stat $DIR_OUTPUT

echo "Ran through OK"
