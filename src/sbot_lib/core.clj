(ns sbot-lib.core
  (:use [overtone.live]))

(require 'overtone.music.pitch)

(defn cheap-exp [num expo]
  (if (= expo 1)
    num
    (* num (cheap-exp num (dec expo)))))

(defsynth bloop [bus 0 note 60 length 0.25]
  (let [env (env-gen:kr (perc 0.02 length) :action FREE)
        sin (sin-osc (midicps note))]
    (out bus (* env sin))))

(def notes [60 62 64 67 69 72 74 76])

(defn choose-note []
  (rand-nth notes))

(defn looper [nome synth]
  (let [beat (nome)]
    (at (nome beat) (synth (choose-note))
    (apply-at (nome (inc beat)) looper nome synth []))))

(defn seq-looper [nome synth group note-seq]
  (let [beat (nome)]
    (at (nome beat) (synth (first note-seq)))
    (apply-at (nome (inc beat)) seq-looper nome synth group (rotate 1 note-seq) [])))

(defn once-through [nome synth note-seq]
  (let [beat (nome)]
    (at (nome beat) (synth (first note-seq)))
    (if (> (count note-seq) 0)
      (apply-at (nome (inc beat)) once-through nome synth (rest note-seq) []))))
