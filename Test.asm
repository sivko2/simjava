*
* Program: TEST
*
* Author:  Roman Verhovsek
*
* Group:   -
*

        ORG    0
        JMP    8
        ORG    8
        LDA  #0         nastavitev vmesnikov
        STA  VHODC      prvi vmesnik je vhoden
        LDA  #1
        STA  IZHODC     drugi vmesnik je izhoden
ZANKA1  LDA  VHODC      branje podatka x
        JPL  ZANKA1
        LDA  VHODD
        STA  X
ZANKA2  LDA  VHODC      branje podatka y
        JPL  ZANKA2
        LDA  VHODD
        STA  Y
        LDA  X
        ADA  Y          pristej y k x
SKOK    STA  IZHODD     izpis rezultata
KONEC   JMP  KONEC
VHODC   EQU  252        kontr. reg. 1. vmesnika
VHODD   EQU  253        podat. reg. 1. vmesnika
IZHODC  EQU  254        kontr. reg. 2. vmesnika
IZHODD  EQU  255        podat. reg. 2. vmesnika
        ORG  112
X       RMB
Y       RMB
        END
* End of program
