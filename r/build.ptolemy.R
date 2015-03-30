source("ptolemy.R")

#modern.sites.sdf <- csv2sdf("csv/modern.csv")
#italy.sdf <- csv2sdf("csv/italy.csv")
#italy.hull <- sdfhull(italy.sdf)

#sicily.sdf <- csv2sdf("csv/sicily.csv")
#sicily.hull <- sdfhull(sicily.sdf)



path="csv"
file.names <- dir(path,pattern=".csv")
for (i in 1:length(file.names)) {
    base.name <- gsub(pattern = ".csv", replacement = "",file.names[i])
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

