; bloopy demo
(demo 10 (let [f (rlpf (dust [12 15]) (+ 1600 (* 1500 (lf-noise1 [1/3, 1/4]))) 0.02 )]
           (comb-l f 1 [0.4 0.35] 2.0)))
