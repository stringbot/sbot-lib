(ns overtone-fun.core
  (:use [overtone.live]))

(definst bloop [note 60 length 0.25]
  (let [env (env-gen:kr (perc 0.01 length) :action FREE)
        sin (sin-osc (midicps note))]
    (* env sin)))

(defn choose-note []
  (let [notes [60 62 64 67 69 72 74 76]]
    (rand-nth notes)))

(defn looper [nome note]
  (let [beat (nome)]
    (at (nome beat) (bloop note))
    (apply-at (nome (inc beat)) looper nome (choose-note) [])))

(looper (metronome 240) (choose-note))
