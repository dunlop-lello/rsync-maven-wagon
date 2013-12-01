package com.pcl_solutions.maven.wagons.rsync_maven_wagon;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Process;
import java.lang.Runtime;
import java.net.URI;
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
      System.out.println("get "+resourceName+" to "+destination.getCanonicalPath());
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
      String sourceArg = source.getCanonicalPath();
      URI uri = new URI(getURL(repository));
      String destArg = getURL(repository)+destination;
      String[] args = {
        "rsync", "--progress", sourceArg, destArg
      };

      Process process = Runtime.getRuntime().exec(args);
      BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader processErrors = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      String line;
      do {
        line = processOutput.readLine();
        if (line != null)
          System.out.println(line);
      } while (line != null);
      process.waitFor();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void putDirectory(File sourceDirectory, String destinationDirectory)
  {
    try {
      String sourceArg = sourceDirectory.getCanonicalPath();
      URI uri = new URI(getURL(repository));
      String destArg = getURL(repository)+destinationDirectory;
      String[] args = {
        "rsync", "-avc", "--progress", sourceArg, destArg
      };

      Process process = Runtime.getRuntime().exec(args);
      BufferedReader processOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader processErrors = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      String line;
      do {
        line = processOutput.readLine();
        if (line != null)
          System.out.println(line);
      } while (line != null);
      process.waitFor();
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

  public String getURL(Repository repository)
  {
    String url = repository.getUrl();
    String protocol = "rsync:";

    if ( url.startsWith(protocol) )
    {
      return url.substring( protocol.length() );
    }
    return url;
  }
}
