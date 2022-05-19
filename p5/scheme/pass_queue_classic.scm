; classic_queue: from p1 implemented in our grammar. Had to change enq and deq to 1 and 2 since our libarys
; does not support eq?. Also had to pass an empty cond to the third expression of our if statement
; This test is pretty comprehensive overall. Makes use of new syntax of define and uses let. Also tests some
; of the trickier parts to implement in the code overall like lambda and cond.
; The output should be:
;  a
;  1
;  2
;  3
;  4
;  5

;1 enq
;2 deq
(define (queue) 
    (let ((head '())(tail '())(x '()));lsh
        (lambda (msg arg)
            (cond
                ((= msg 1);enqueue
                    (if (null? head);if no elements in queue
                        (begin (set! head (cons arg '())) (set! tail head));set tail to the head, set the head to x 
                        (begin 
                            (set! x (cons arg '()));creates new pair x
                            (set-cdr! head x);sets the head to point towards x
                            (set! head x));sets head to point towards the new pair x
                    )
                )
                ((= msg 2);dequeue
                    (set! x (car tail)); sets x the to car of tail which is the value
                    (set! tail (cdr tail));sets the tail to the element before the tail
                    (if(null? tail);if the tail is empty
                        (begin (set! head '()) (set! tail '()))
                        (cond );HERE IS A HACK FOR OUR VERISON OF THE GRAMMAR
;OUR VERSION OF THE GRAMMAR REQUIRES AN IF TO BE PASSED 3 EXPRESSIONS. SINCE WE DONT WANT TO DO ANYTHING
;IF THE TAIL IS NOT NULL WE USE A COND AS A HACK FOR AN EMPTY 3RD EXPRESSION SINCE COND IS THE ONLY PART OF OUR
;VERSION OF SCHEME THAT RETURNS NULL AND DOES NOT MESS WITH THE ENV
                    )
                    x;return the value
                )
            )
        )
    )
)

; Then you can issue commands such as:
  (define q (queue))
  (q 1 #\a)
  (q 2 '())
  (q 1 1)
  (q 1 2)
  (q 1 3)
  (q 1 4)
  (q 1 5)
  (q 2 '())
  (q 2 '())
  (q 2 '())
  (q 2 '())
  (q 2 '())