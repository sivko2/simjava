* Citat iz ucbenika "OSNOVE RACUNALNIKOV ZA ELEKTROTEHNIKE" na strani 74:
*
* 5.6 ZGLEDI ZBIRNISKIH PROGRAMOV
*
* Za prvi zgled sestavimo program, po katerem racunalnik prebere z vhodne
* naprave tri osembitna predznacena stevila x, y in z v napisanem vrstnem redu
* ter izracuna in izpise na izhodni napravi vrednost podatka t, ki je dolocena
* z izrazom
*
*                         | 4(x-z), za z>0
*                     t = |
*                         | 4(y-z), za z<=0
*
* Ce je rezultat t prekoracil obseg osembitnih predznacenih stevil, naj bo
* izhodna vrednost t enaka nic.
*
* Nalogo resimo tako, da najprej nastavimo periferne vmesnike in preberemo iz
* podatkovnega registra vhodnega perifernega vmesnika zapovrstjo tri podatke,
* kot smo naredili ze v prejsnem zgledu. Zatem izracunamo t po dveh poteh
* glede na z. Ker se oba izraza za t razlikujeta le v spremenljivki, od katere
* odstevamo z, se poti razlikujeta samo v ukazu, katero spremenljivko nalozimo
* v akumulator. Pri tem seveda za imena celic uporabljamo kar imena
* spremenljivk. Opozoriti velja, da v izrazu za t nastopa pogoj z>0, ki ga z
* nasimi skocnimi ukazi ne moremo zapisati, zato ga sestavimo s konjunkcijo
* dveh pogojev ((Z<>0) and (Z>=0)). Mnozenje s 4 dosezemo z dvakratnim
* sestevanjem, pri cemer po vsakem sestevanju perverimo prelivni bit. Rezultat
* nato prenesemo iz akumulatorja v podatkovni register izhodnega perifernega
* vmesnika. Ker je ta izpis prvi, niti ni treba perveriti, ali je vmesnik
* pripravljen za prenos podatka. Zbirniski program za prvi zgled je:
*
         ORG  0
         LDA  #0         nastavitev vmesnikov
         STA  VHODC      prvi vmesnik je vhoden
         LDA  #1
         STA  IZHODC     drugi vmesnik je izhoden
ZANKA1   LDA  VHODC      branje podatka x
         JPL  ZANKA1
         LDA  VHODD
         STA  X
ZANKA2   LDA  VHODC      branje podatka y
         JPL  ZANKA2
         LDA  VHODD
         STA  Y
ZANKA3   LDA  VHODC      branje podatka z
         JPL  ZANKA3
         LDA  VHODD
         STA  Z
         JZE  SKOK1      skok, ce je z==0
         JMI  SKOK1      skok, ce je z<0
         LDA  X
         JMP  SKOK2
SKOK1    LDA  Y
SKOK2    SBA  Z
         JVS  SKOK3      skok, ce je preliv
         STA  RAZL       prvo mnozenje z 2
         ADA  RAZL
         JVS  SKOK3      skok, ce je preliv
         STA  RAZL       drugo mnozenje z 2
         ADA  RAZL
         JVS  SKOK3      skok, ce je preliv
         JMP  SKOK4      skok na izpis
SKOK3    LDA  #0
SKOK4    STA  IZHODD     izpis rezultata
KONEC    JMP  KONEC
VHODC    EQU  252        kontr. reg. 1. vmesnika
VHODD    EQU  253        podat. reg. 1. vmesnika
IZHODC   EQU  254        kontr. reg. 2. vmesnika
IZHODD   EQU  255        podat. reg. 2. vmesnika
         ORG  112
X        RMB
Y        RMB
Z        RMB
RAZL     RMB
         END
* .... konec citata.
*
