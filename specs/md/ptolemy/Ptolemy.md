# Specifications for the Ptolemy Machine, version @version@ #

The initial focus of the Ptolemy Machine  is Ptolemy's *Geography*.

The first task to specify is extracting Ptolemy's data set from the archival TEI XML edition of the text.

From an XML source text, we need to extract `list` items that contain paired `name` and `measure` elements.

Citation hierarchy is book/chapter/section in `div/div/div`.  Books ~ continents, chapters ~ provinces, sections ~ geotype.

Rather than adding ever more markup to the XML, we will annotate these analytically.

We will also align this edition with other analyses of Ptolemy.

Sample markup.



    <div  ana='#massalia' n='2' type='paralios'>
                       <p > Περιγράφεται δὲ ἡ παράλιος πᾶσα τὸν τρόπον τοῦτον· μετὰ τὰς
                            τοῦ Οὐάρου ποταμοῦ ἐκβολὰς ἐν τῷ Λιγυστικῷ πελάγει <rs  type='ethnic'>Μασσαλιωτῶν</rs>
                        </p>
                        <list  ana='#liguriansea' type='simple'>
                            <item >
                                <name  key='pt-ll-1114' type='place'>Νίκαια</name>
                                <measure  type='llpair'>
                                    <num  type='cardinal'>κη</num>
                                    <num  type='fraction'/>
                                    <num  type='cardinal'>μγ</num>
                                    <num  type='fraction'>ιβ</num>
                                </measure>
                            </item>



