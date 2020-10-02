(ns partitioner.examples
   (:require
   [clojure.core.async :as async]))

; the following functions are used to consume the data yielded from the the partition-to-channels function
(defn chunk-reader [channel]
  (loop [element (async/<!! channel)]
    (when element
      (prn element)
      (recur (async/<!! channel))))
  (prn "channel exhausted"))


(defn reader [channel]
  (loop [chunk-channel (async/<!! channel)]
    (when chunk-channel
      (chunk-reader chunk-channel)
      (recur (async/<!! channel)))))




