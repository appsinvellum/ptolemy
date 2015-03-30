source("ptolemy.R")

# Cycle through all .csv files in the csv directory,
# and generate .sdf and .hull files for each.

path="csv"
file.names <- dir(path,pattern=".csv")
for (i in 1:length(file.names)) {

    base.name <- gsub(pattern = ".csv", replacement = "",file.names[i])
    print (paste("Process", base.name))
    assign(paste(base.name,".sdf",sep=""), csv2sdf(paste("csv",file.names[i], sep="/")))
    sdf <- as.name(paste(base.name,".sdf",sep=""))
    assign(paste(base.name,".hull",sep=""), sdfhull(eval(sdf)))
}


# Clean up:
rm(i)
rm(file.names)
rm (path)
rm (base.name)
rm(sdf)

