# Read Ptolemy data from .csv file,
# return a spatial data frame
csv2sdf <- function(csvfile) {
    require("sp")
    sites.df <- read.csv(file=csvfile, head=TRUE, quote="'")
    coords.df <- sites.df[,c("Lon","Lat")]
    # Create df of attributes, distinguishing factors and chrs
    site.labels <- as.character(sites.df[,c("Label")] )
    site.urns <- sites.df[,"SiteId"]
    # return value
    site.attrs.df <- data.frame(site.urns,site.labels,stringsAsFactors=FALSE)
    SpatialPointsDataFrame(coords.df, site.attrs.df)
}


# Find convex hull from a SpatialDataFrame
sdfhull <- function(sdf) {
    coords <- sdf@coords
    con.hull.pos <- chull(coords)
    # return convex hull:
    hull.coords <- coords[c(con.hull.pos, con.hull.pos[1]),]
    #rbind(coords[con.hull.pos,], coords[con.hull.pos[1],])
}


# gets vector of names for files
# organized by province
provdatanames <- function(path) {
    provnames <- c()
    file.names <- dir(path,pattern=".csv")
    for (i in 1:length(file.names)) {
        base.name <- gsub(pattern = ".csv", replacement = "",file.names[i])
        provnames <- c(provnames, base.name)
    }
    provnames
}


wgs84 <- function() {
    CRS("+proj=longlat +ellps=WGS84 +datum=WGS84")
}

bonne36 <- function() {
    CRS("+proj=bonne +ellps=WGS84 +datum=WGS84 +lat_1=36 +lon_0=25")
}


