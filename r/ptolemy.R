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
    rbind(coords[con.hull.pos,], coords[con.hull.pos[1],])
}


