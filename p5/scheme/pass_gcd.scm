;gcd from p1 reworked to work with only p4 grammar.
;should return 5
(define gcd (lambda (x y) (if (= y 0) x (gcd y (% x y))))) (gcd 15 10)