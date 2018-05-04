package uk.co.a1dutch.pipeline

public class Helm implements Serializable {
  private steps
  private String namespace

  Helm(steps, String namespace=null) {
    this.steps = steps
    this.namespace = namespace
  }

  def deploy(File directory) {
    validateDirectory(directory)

    File chartDirectory = new File(directory, "charts");
    validateDirectory(chartDirectory)

    File[] files = chartDirectory.listFiles()
    files.each { chart ->
      if (chart.isDirectory()) {
        install(chart.getName())
      }
    }
  }

  def install(String chart) {
    if (chart == null) {
      throw new Exception("chart name must be provided")
    }
    println "deploying helm chart: chart/${chart}}" + (namespace ? ", namespace: ${namespace}" : "" )
    steps.sh("helm upgrade ${chart} --install charts/${chart} --debug" + (namespace ? " --namespace=${namespace}" : "" ))
  }

  def validateDirectory(File directory) {
    if (!directory.exists() || !directory.isDirectory()) {
      throw new Exception("$directory must be a directory")
    }
  }
}
