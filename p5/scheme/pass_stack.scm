; stack: implement a stack in Scheme with our grammar, had to make some changes as 
; there is no eq? in our standard librarys. Instead I changed the actions of pop,push,and top to numbers
; This test is pretty comprehensive overall. Makes use of new syntax of define and uses let. Also tests some
; of the trickier parts to implement in the code overall like lambda and cond.
; The output should be:
;  d
;  c
;  e
;  a

;1 is pop
;2 is push
;3 is top
(define (stack) 
    (let ((stack '()))
        (lambda (msg arg)
            (cond
                ((= msg 1) (if (null? stack); if the stack is null, print an error. Can't add to empty stack
                    "Error: stack is null" ; print out stack is null
                      (set! stack (cdr stack)))); sets the stack to it's cdr(the rest of list to stack)
                ((= msg 2) (set! stack (cons arg stack))); sets the stack first value to arg, and the rest is stack
                ((= msg 3) (if (null? stack); if the stack is null, print an error. Can't get value from empty stack
                     "Error: stack is null"; print out stack is null
                     (car stack)))))));prints out the car(first value) of the stack
;testing the newly created stack
(define s (stack))
(begin (s 2 #\a ) (s 2 #\b ) (s 2 #\c ) (s 2 #\d ))
(s 3 '())
(s 1 '())
(s 3 '())
(s 2 #\e )
(s 3 '())
(begin (s 1 '()) (s 1 '()) (s 1 '()))
(s 3 '())
(s 1 '())