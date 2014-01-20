(ns sbot-lib.fm-ops
  (:use [overtone.live]
   :require [sbot-lib.reverb :as verb]))

(defn amp-sine [hz amp]
  "scales a sine oscillator with the specified hz by amp"
  (let [* #'overtone.sc.ugen-collide/*]
    (* (sin-osc hz) amp)))

(defn fm-op [hz1 amp1 hz2 amp2]
  "use an amp-sine to modulate the frequency of another amp-sine"
  (let [+ #'overtone.sc.ugen-collide/+]
    (amp-sine (+ hz1 (amp-sine hz2 amp2)) amp1)))

(defsynth fm-perc [bus 0 note 60 length 0.25 mod-hz 100 mod-amp 200]
  "FM percussion synth"
  (let [env (env-gen:kr (perc 0.03 length) :action FREE)
        fmop (fm-op (midicps note) 1.0 mod-hz mod-amp)]
    (out bus (* 0.7 env fmop))))

(defn fm-looper [nome n-notes max-count synth group note-seq len-seq mod-seq mod-amp-seq]
  (let [beat (nome)
        note (first note-seq)
        len  (first len-seq)
        fmod (first mod-seq)
        fm-amp (first mod-amp-seq)]
    (at (nome beat) (synth [:head group] 0 note len fmod fm-amp))
    (if (< n-notes max-count)
      (apply-at (nome (inc beat))
                fm-looper
                nome
                (inc n-notes)
                max-count
                synth
                group
                (rotate 1 note-seq)
                (rotate 1 len-seq)
                (rotate 1 mod-seq)
                (rotate 1 mod-amp-seq) []))))

(defonce fm-grp (group "OOTC Group"))

(defn fm-loop [max-count tempo]
  (fm-looper (metronome tempo)
              1
              max-count
              fm-perc
              fm-grp
              (shuffle [60 64 72 76 84 90])
              (shuffle [0.1 0.3 0.6 0.2 1.0])
              (shuffle [200 400 300 800 1200 2000 4100])
              (shuffle [1200 800 900])))

(defn insert-reverb []
  (verb/schroederverb [:tail fm-grp] :bus 0 :decay 4.0))

(defn fm-party [max-count]
  (do
    (fm-loop max-count 240)
    (insert-reverb)))
