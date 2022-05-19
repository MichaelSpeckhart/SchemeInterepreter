;nested let test
;should return 35
(let ((x 2) (y 3)) (let ((x 7) (z (+ x y))) (* z x)))               