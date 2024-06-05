# JavaGame

More informations, requirements etc, in the **[Wiki](https://github.com/DHBWProjectsIT23/JavaGame/wiki)**!

# Erste Besprechung
Texturen/Grafik
-
- Sci/Fi Roboter
- Probetruppen
- Maps CSV?

Struktur
-
- Super-Klasse Truppen
    - Boden-Truppen
    - Luft-Truppen
- Werte, State, Position
- CSV/Map Dateiladen
- Terrain Klasse
    - Arten von Terrain
- Cache?

Schadensberechnung:
- 
Um das Nutzen einer Schadensmatrix (wie im Originalspiel)
zu umgehen wird der "BaseDamage" mit hilfe einiger Hilfswerte
selbst berechnet. Die dabei entstandenen Werte weichen in den
meisten Fällen kaum von den Originalwerten ab, lediglich an
einigen Stellen sind größere Unterschiede zu erkennen, die 
das Spiel jedoch nicht großartig beeinflussen.