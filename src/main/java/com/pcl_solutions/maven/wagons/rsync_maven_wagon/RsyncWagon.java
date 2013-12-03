package com.pcl_solutions.maven.wagons.rsync_maven_wagon;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Process;
import java.lang.Runtime;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.events.SessionListener;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.proxy.ProxyInfo;
import org.apache.maven.wagon.proxy.ProxyInfoProvider;
import org.apache.maven.wagon.repository.Repository;

class RsyncWagon implements Wagon {
  private List<SessionListener> sessionListeners;
  private List<TransferListener> transferListeners;
  private Repository repository;

  public RsyncWagon()
  {
    sessionListeners = new Vector<SessionListener>();
    transferListeners = new Vector<TransferListener>();
  }

  public void addSessionListener(SessionListener listener)
  {
    sessionListeners.add(listener);
  }

  public void addTransferListener(TransferListener listener)
  {
    transferListeners.add(listener);
  }

  public void connect(Repository source)
  {
    repository = source;
  }

  public void connect(Repository source, ProxyInfo proxyInfo)
  {
    connect(source);
  }

  public void connect(Repository source, ProxyInfoProvider proxyInfoProvider)
  {
    connect(source);
  }

  public void connect(Repository source, AuthenticationInfo authenticationInfo)
  {
    connect(source);
  }

  public void connect(Repository source, AuthenticationInfo authenticationInfo, ProxyInfo proxyInfo)
  {
    connect(source);
  }

  public void connect(Repository source, AuthenticationInfo authenticationInfo, ProxyInfoProvider proxyInfoProvider)
  {
    connect(source);
  }

  public void disconnect()
  {
  }

  public void get(String resourceName, File destination)
  {
    try {
      runRsync(
        getURL(repository)+File.separator+resourceName,
        destination.getCanonicalPath(),
        Arrays.asList("-aR", "--dirs", "--progress"));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public List<String> getFileList(String destinationDirectory)
  {
    return new Vector<String>();
  }

  public boolean getIfNewer(String resourceName, File destination, long timestamp)
  {
    try {
      System.out.println("getIfNewer "+resourceName+" to "+destination.getCanonicalPath());
    } catch (Exception e) {
      System.out.println(e);
    }
    return false;
  }

  public int getReadTimeout()
  {
    return 0;
  }

  public Repository getRepository()
  {
    return null;
  }

  public int getTimeout()
  {
    return 0;
  }

  public boolean hasSessionListener(SessionListener listener)
  {
    return sessionListeners.contains(listener);
  }

  public boolean hasTransferListener(TransferListener listener)
  {
    return transferListeners.contains(listener);
  }

  public boolean isInteractive()
  {
    return false;
  }

  public void openConnection()
  {
  }

  public void put(File source, String destination)
  {
    try {
      runRsync(
        source.getCanonicalPath(),
        getURL(repository)+File.separator+destination,
        Arrays.asList("-aR", "--dirs", "--progress"));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void putDirectory(File sourceDirectory, String destinationDirectory)
  {
    try {
      runRsync(
        sourceDirectory.getCanonicalPath(),
        getURL(repository)+File.separator+destinationDirectory,
        Arrays.asList("--rsync-path=\"mkdir -p "+getURL(repository)+"\" && rsync", "-avc", "--progress"));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void removeSessionListener(SessionListener listener)
  {
  }

  public void removeTransferListener(TransferListener listener)
  {
  }

  public boolean resourceExists(String resourceName)
  {
    return true;
  }

  public void setInteractive(boolean interactive)
  {
  }

  public void setReadTimeout(int timeoutValue)
  {
  }

  public void setTimeout(int timeoutValue)
  {
  }

  public boolean supportsDirectoryCopy()
  {
    return true;
  }

  private String getURL(Repository repository)
  {
    String url = repository.getUrl();
    String protocol = "rsync:";

    if ( url.startsWith(protocol) )
    {
      return url.substring( protocol.length() );
    }
    return url;
  }

  private void runRsync(String source, String destination, List<String> options) throws Exception
  {
    ArrayList<String> argv = new ArrayList<String>(options);
    String line;
    Process process;
    BufferedReader processOutput, processErrors;
    argv.add(0, "rsync");
    argv.add(source);
    argv.add(destination);
    process = Runtime.getRuntime().exec(argv.toArray(new String[0]));
    processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
    processErrors = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    do {
      line = processOutput.readLine();
      if (line != null)
        System.out.println(line);
    } while (line != null);
    do {
      line = processErrors.readLine();
      if (line != null)
        System.out.println(line);
    } while (line != null);
    process.waitFor();
  }
}
