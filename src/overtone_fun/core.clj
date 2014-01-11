(ns overtone-fun.core
  (:use [overtone.live]))

(require 'overtone.music.pitch)

(definst bloop [note 60 length 0.25]
  (let [env (env-gen:kr (perc 0.01 length) :action FREE)
        sin (sin-osc (midicps note))]
    (* env sin)))

(def notes [60 62 64 67 69 72 74 76])

(defn choose-note []
  (rand-nth notes))

(defn looper [nome synth]
  (let [beat (nome)]
    (at (nome beat) (synth (choose-note))
    (apply-at (nome (inc beat)) looper nome synth []))))

(defn seq-looper [nome synth note-seq]
  (let [beat (nome)]
    (at (nome beat) (synth (first note-seq)))
    (apply-at (nome (inc beat)) seq-looper nome (rotate 1 note-seq) [])))

(defn once-through [nome synth note-seq]
  (let [beat (nome)]
    (at (nome beat) (synth (first note-seq)))
    (if (> (count note-seq) 0)
      (apply-at (nome (inc beat)) once-through nome synth (rest note-seq) []))))
