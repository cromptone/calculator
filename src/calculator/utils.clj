(ns calculator.utils
  (:import java.util.Base64))

(defn decode-64 [encoded-str]
  (try
    (->> encoded-str
         (.decode (Base64/getDecoder))
         String.)
    (catch Exception e "Error while decoding query")))
