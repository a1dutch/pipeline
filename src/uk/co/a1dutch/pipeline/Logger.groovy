package uk.co.a1dutch.pipeline

public class Logger implements Serializable {
  private steps

  Logger(steps) {
    this.steps = steps
  }

  def error(def object) {
    log('ERROR', object)
  }

  def info(def object) {
    log('INFO', object)
  }

  def warn(def object) {
    log('WARN', object)
  }

  def debug(def object) {
    log('DEBUG', object)
  }

  def log(String level, def object) {
    steps.echo("[${level}] ${object}")
  }

}
