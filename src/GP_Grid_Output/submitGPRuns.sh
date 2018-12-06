for((i=0; i<100; i++))
do
qsub GPRuns.sh $i output
#qsub NHBSA1.sh $i
done
