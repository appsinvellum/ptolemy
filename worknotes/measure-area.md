## Successfully built polys like this ##

1. it.hull <- sdfhull(Italia.sdf)
2. it.poly <- SpatialPolygons(list(Polygons(list(Polygon(it.hull)), ID=1)))
3. it.poly.df <- SpatialPolygonsDataFrame(it.poly,data=data.frame(ID=1))


This gets area!

    sapply(slot(it.poly.df, 'polygons'), function(i) slot(i, 'area')) 


### Calc area w projection ###

The magic general one:

    crs.wgs84  <- CRS("+proj=longlat +ellps=WGS84 +datum=WGS84") 

    crs.sphere <- CRS("+proj=longlat +ellps=sphere +no_defs"))
    crs.laea <- CRS("+proj=laea +lon_0=20 +lat_0=5 +ellps=WGS84 +datum=WGS84")


+proj=aea +lat_1=43 +lat_2=62 +lat_0=30 +lon_0=10 +x_0=0 +y_0=0 +ellps=intl +units=m +no_defs
    
    proj4string(modern.sdf) <- wgs84

Bivand: <https://stat.ethz.ch/pipermail/r-sig-geo/2011-January/010789.html>

## Possible how-to ##

<http://stackoverflow.com/questions/25606512/create-polygon-from-points-and-save-as-shapefile>


library("sp")
library("rgdal")

sp_poly <- SpatialPolygons(list(Polygons(list(Polygon(coords)), ID=1)))
# set coordinate reference system with SpatialPolygons(..., proj4string=CRS(...))
# e.g. CRS("+proj=longlat +datum=WGS84") #


sp_poly_df <- SpatialPolygonsDataFrame(sp_poly, data=data.frame(ID=1))
writeOGR(sp_poly_df, "chull", layer="chull", driver="ESRI Shapefile")


## Think about ##

 #########R-code########### ## load rgdal package 
library(rgdal) 
## load your polygone shapefile ##

dsn<-"/wherever/your/files/are/directory" 
ogrListLayers(dsn) 

# list the layers in the above directory #

lol<-readOGR(dsn,"lol") 
# shapefile name is lol #

# check your data #

summary(lol) 
getClass("Polygon") 
ur.area<-sapply(slot(lol, "polygons"), function(x) sapply(slot(x, "Polygons"), slot, "area")) #check the areas 
str(ur.area)
 #########END of R-code########### 



Nice on projectison: <http://wiki.tcl.tk/19833>

## Other
Good course from Colorado <http://www.colorado.edu/geography/class_homepages/geog_4023_s11/lectures.html>


Consider projecting to lambert equal area azimuthal and measuring density there.

<http://www.r-bloggers.com/plot-maps-like-a-boss/>

<http://robinlovelace.net/r/2014/03/21/clustering-points-R.html>


Probabl stupid: <https://www.academia.edu/4979335/Ptolemy_s_Geographia_in_digits>