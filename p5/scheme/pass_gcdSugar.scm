;gcd from p1. Had to replace remaineder with %. Makes use of new syntax for define.
;Should return 8
(define (gcd x y) 
    (if ( = y 0)  x (gcd y (% x y
    )
    )
    )
    ) (gcd 24 16)
        ; if y = 0
; print x