;symantic sugar for design test copied from piazza
;should return 8
(define (make-adder num)
  (lambda (x) (+ x num)))
((make-adder 5 )3)