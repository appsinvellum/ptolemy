package ptolemy

import edu.holycross.shot.ptolemy.*

class FullCsv {

  /*
     arg[0] = XML source file
     arg[1] = repository's collection directory
     arg[2] = output directory
  */
  static void main(args) {
    File xml = new File(args[0])
    File collections = new File(args[1])
    File outputDir = new File(args[2])

    def modernList = Composer.modernComposite(xml,collections)

    File lists = new File(outputDir, "ptolemy.csv")
    CsvManager csvm = new CsvManager()
    lists.setText(csvm.compoundSitesToCsv(modernList), "UTF-8")
  }
}
