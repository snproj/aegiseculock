(ns aegiseculock.core
  (:gen-class)
  (:require [clojure.java.shell :as shell]
            [clojure.string :as str]))

(defn flip [f & args]
  (apply f (reverse args)))

(defmacro sophisticated-permissions-security-procedure-injection [dot-filename actions]
  (list 'do
   (list 'shell/sh "chmod" "777" dot-filename)
   (list 'let ['result actions]
         (list 'shell/sh "chmod" "000" dot-filename)
         'result)))

(defmacro sophisticated-read [dot-filename]
  (list 'sophisticated-permissions-security-procedure-injection dot-filename (list 'slurp dot-filename)))

(defmacro sophisticated-write [dot-filename body]
(list 'sophisticated-permissions-security-procedure-injection dot-filename (list 'spit dot-filename body)))

(def key-extension ".SecuLock")
(def master-password-filename ".AEGISecuLock_R4ND0MH45H")

(defn set-up-authentication []
  (println "Enter a password for first-time setup:")
  (let [self-password (read-line)]
    (spit master-password-filename
          self-password)
    (shell/sh "chmod" "000" master-password-filename)))

(defn file-exists? [dot-filename]
(let [result (:exit (shell/sh "test" "-f" dot-filename))]
  (if (= result 0)
    true
    false)))

(defn first-time-check []
  (file-exists? master-password-filename))

(defn authenticate-login? []
  (let [dubious-password (read-line)
        retrieved-password (sophisticated-read master-password-filename)]
    (= dubious-password retrieved-password)))

(defn display-files []
  (->> (str/split
        (:out (shell/sh "ls" "-a"))
        #"\n")

       (filter
        (partial flip str/ends-with? key-extension))

       (map
        (partial drop-last (count key-extension)))

       (map
        (partial drop 1))

       (map
        (partial apply str))

       (apply println)))

(defn set-password [new-password dot-filename]
  (let [filename-seculock (str dot-filename key-extension)]
    (if (file-exists? filename-seculock)
      (sophisticated-write filename-seculock new-password)
      (do
        (shell/sh "touch" filename-seculock)
        (sophisticated-write filename-seculock new-password)))))

(defn new-file []
(println "Please enter new file name.")
(let [new-filename (read-line)
      new-dot-filename (str "." new-filename)]
  (sophisticated-write new-dot-filename "NEW FILE")
  (println "Please enter new password.")
  (let [new-password (read-line)]
    (set-password new-password new-dot-filename))))

(defn try-password? [dot-filename]
  (println "Enter current password.")
  (let [filename-seculock (str dot-filename key-extension)
        retrieved-password (sophisticated-read filename-seculock)
        dubious-password (read-line)]
    (= dubious-password retrieved-password)))

(defn reset-master-password []
  (println "Please enter old master password")
  (if (authenticate-login?)
    (do (println "Please enter new master password.")
        (let [new-password (read-line)]
          (sophisticated-write master-password-filename new-password)))
    (println "Wrong password OR filename!")))

(defn read-file []
(println "Please type the name of a file.")
(let [choice-filename (read-line)
      dot-filename (str "." choice-filename)]
  (if (and (file-exists? dot-filename) (try-password? dot-filename))
    (sophisticated-read dot-filename)
    (println "Wrong password OR filename!"))))

(defn write-file []
  (println "Please type the name of a file.")
  (let [choice-filename (read-line)
        dot-filename (str "." choice-filename)]
    (if (and (file-exists? dot-filename) (try-password? dot-filename))
      (do
        (println "Enter the text to write.")
        (let [write-body (read-line)]
          (sophisticated-write dot-filename write-body)))
      (println "Wrong password OR filename!"))))

(defn change-file-password []
  (println "Please type the name of a file.")
  (let [choice-filename (read-line)
        dot-filename (str "." choice-filename)]
    (if (and (file-exists? dot-filename) (try-password? dot-filename))
      (do
        (println "Enter new password.")
        (let [new-password (read-line)]
          (set-password new-password dot-filename)))
      (println "Wrong password OR filename!"))))

(defn exit []
  (println "Exiting AEGISecuLock."))

(defn main-menu []
(println
 "CHOOSE:
1 -> Reset Master Password
2 -> New File
3 -> Read File
4 -> Write to File
5 -> Change File Password
6 -> Exit")
(let [choice (read-line)]
  (case choice
    "1" (reset-master-password)
    "2" (new-file)
    "3" (read-file)
    "4" (write-file)
    "5" (change-file-password)
    "6" (exit)
    (main-menu))))

(defn -main [] (main-menu))

