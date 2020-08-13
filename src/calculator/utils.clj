(ns calculator.utils
  (:import java.util.Base64))

(defn decode-64 [encoded-str]
  (->> encoded-str
       (.decode (Base64/getDecoder))
       String.))
