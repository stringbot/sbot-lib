(ns overtone-fun.fm-ops
  (:use [overtone.live]
   :require [overtone-fun.reverb]))

(defmacro amp-sine [hz amp]
  (let [* #'overtone.sc.ugen-collide/*]
  `(~* (sin-osc ~hz) ~amp)))

(defmacro fm-op [hz1 amp1 hz2 amp2]
  (let [+ #'overtone.sc.ugen-collide/+]
    `(amp-sine (~+ ~hz1 (amp-sine ~hz2 ~amp2)) ~amp1)))

(definst fm-perc [note 60 length 0.25 mod-hz 100 mod-amp 200]
  (let [env (env-gen:kr (perc 0.01 length) :action FREE)
        fmop (fm-op (midicps note) 1.0 mod-hz mod-amp)]
    (* env fmop)))

(defn looper [nome synth note-seq len-seq mod-seq mod-amp-seq]
  (let [beat (nome)
        note (first note-seq)
        len  (first len-seq)
        fmod (first mod-seq)
        fm-amp (first mod-amp-seq)]
    (at (nome beat) (synth note len fmod fm-amp))
    (apply-at (nome (inc beat))
              looper
              nome
              synth
              (rotate 1 note-seq)
              (rotate 1 len-seq)
              (rotate 1 mod-seq)
              (rotate 1 mod-amp-seq) [])))


(do
  (looper (metronome 240)
          fm-perc
          [60 64 72 76 84 90]
          [0.1 0.3 0.6 0.2 1.0]
          [200 400 300 800 1200 2000 4100]
          [1200 800 900])
  (overtone-fun.reverb/schroederverb [:tail] :bus 0 :decay 4.0))
