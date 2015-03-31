source("ptolemy.R")

# Cycle through all .csv files in the csv directory,
# and generate .sdf and .hull files for each.



base.names <- provdatanames("csv")
for (i in 1:length(base.names)) {
    print (paste("Process", base.names[i]))
    assign(paste(base.names[i],".sdf",sep=""), csv2sdf(paste("csv/",base.names[i],".csv", sep="")))
    sdf <- as.name(paste(base.names[i],".sdf",sep=""))
    assign(paste(base.names[i],".hull",sep=""), sdfhull(eval(sdf)))
}


# Clean up:

rm (base.names)
rm(i)
rm(sdf)

