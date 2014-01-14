(ns sbot-lib.reverb
  (:use [overtone.live]))

(definst cverb [bus 0 dly 0.1 scale 0.5]
  (out [bus (+ bus 1)] ;; force it to stereo
       (* scale
          (+ (in bus)
             (comb-n (in bus)
               1.0
               dly
               2.0)))))

(defn s25ktos [samps]
  (/ samps 25000.0))

(defn s-allpass [in amp samps decay]
  (let [sc* #'overtone.sc.ugen-collide/*
        ms (s25ktos samps)]
    (sc* amp (allpass-n in ms ms decay))))

(defn s-comb [in amp samps decay]
  (let [sc* #'overtone.sc.ugen-collide/*
        ms (s25ktos samps)]
    (sc* amp (comb-n in ms ms decay))))

(defsynth schroederverb [bus 0 decay 1.0]
  (let [in (in bus)
        a1 (s-allpass in 0.7 347 decay)
        a2 (s-allpass a1 0.7 113 decay)
        a3 (s-allpass a2 0.7 37 decay)
        c1 (s-comb a3 0.773 1687 decay)
        c2 (s-comb a3 0.802 1601 decay)
        c3 (s-comb a3 0.753 2053 decay)
        c4 (s-comb a3 0.733 2251 decay)]
    (out [bus (+ bus 1)] [(+ c1 c4) (+ c2 c3)])))
