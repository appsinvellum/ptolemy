png(file="provinces.png", bg="transparent", width=1250, height=668)


plot(modern.sdf@coords)

color.names <- c("red", "blue", "yellow", "green", "darkred","darkblue","darkorange","darkgreen" )

# For each file in .csv, get base name and plot corresponding .hull file


path="csv"
file.names <- dir(path,pattern=".csv")
for (i in 1:length(file.names)) {
    if (file.names[i] != "modern.csv") {
        color.idx <- i %% length(color.names)
        base.name <- gsub(pattern = ".csv", replacement = "",file.names[i])
        print (paste(i,": ", file.names[i],":", color.names[color.idx + 1]))
        hull <- as.name(paste(base.name,".hull",sep=""))
        lines(eval(hull), col=color.names[color.idx + 1],lwd=3)
    }
}


dev.off()


rm(i)

