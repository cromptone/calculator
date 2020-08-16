(ns calculator.utils
  (:import java.util.Base64))

(defn decode-64 [encoded-str]
  (->> encoded-str
       (.decode (Base64/getDecoder))
       String.))

(defn encode-64 [unencoded-str]
  (.encodeToString (Base64/getEncoder) (.getBytes unencoded-str)))
