for each row in prec.df

- counts are: as.numeric(prec.df[ROW,3:14])
- label is:  prec.df[ROW,2]


barplot(counts, main="Car Distribution", 
  	xlab="Number of Gears")