(ns partitioner.core
  (:require
   [clojure.core.async :as async]))

(defn partition-to-channels [input-channel partition-size]
  (let [output-channel (async/chan 2)]
    (async/go-loop [count 0
                    element (async/<! input-channel)
                    partition-channel (async/chan partition-size)]
      (when (= 0 count)
        (async/>! output-channel partition-channel))

      (if element
        (if (= count partition-size)
          (do
            (async/close! partition-channel)
            (recur 0 element (async/chan partition-size)))
          (do
            (async/>! partition-channel element)
            (recur (inc count) (async/<! input-channel) partition-channel)))
        (do (async/close! partition-channel)
            (async/close! output-channel))))
    output-channel))
