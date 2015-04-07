
csv2gvis <- function(csvfile) {
    require("googleVis")
    sites.df <- read.csv(file=csvfile, head=TRUE, quote="'")
    gvisMap(sites.df, "LatLon", "Site",    options=list(mapType='satellite', useMapTypeControl=TRUE))
}
