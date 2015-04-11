source("ptolemy.R")

color.names <- c("red", "blue", "yellow", "green", "darkred","darkblue","darkorange","darkgreen" )

svg(file="provinces.svg", width=13, height=7)
plot(modern.sdf@coords)

# For each file in .csv, get base name and plot corresponding .hull file
base.names <- provdatanames("csv")
for (i in 1:length(base.names)) {
    if (base.names[i] != "modern") {
        color.idx <- i %% length(color.names)
        print (paste(i,": ", base.names[i],":", color.names[color.idx + 1]))
        hull <- as.name(paste(base.names[i],".hull",sep=""))
        lines(eval(hull), col=color.names[color.idx + 1],lwd=3)
    }
}
dev.off()

rm(base.names)
rm(i)
rm(color.idx)
rm(hull)
