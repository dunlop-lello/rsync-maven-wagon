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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.events.SessionListener;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.proxy.ProxyInfo;
import org.apache.maven.wagon.proxy.ProxyInfoProvider;
import org.apache.maven.wagon.repository.Repository;

public class RsyncWagon implements Wagon {
  private List<SessionListener> sessionListeners;
  private List<TransferListener> transferListeners;
  private Repository repository;
  private AuthenticationInfo authenticationInfo;
  private ProxyInfo proxyInfo;
  private ProxyInfoProvider proxyInfoProvider;
  private String protocol;
  private String host;
  private String path;

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

  public void connect(Repository repository)
  {
    setRepository(repository);
  }

  public void connect(Repository repository, ProxyInfo proxyInfo)
  {
    setRepository(repository);
  }

  public void connect(Repository repository, ProxyInfoProvider proxyInfoProvider)
  {
    setRepository(repository);
  }

  public void connect(Repository repository, AuthenticationInfo authenticationInfo)
  {
    setRepository(repository);
  }

  public void connect(Repository repository, AuthenticationInfo authenticationInfo, ProxyInfo proxyInfo)
  {
    setRepository(repository);
  }

  public void connect(Repository repository, AuthenticationInfo authenticationInfo, ProxyInfoProvider proxyInfoProvider)
  {
    setRepository(repository);
  }

  public void disconnect()
  {
  }

  public void get(String resourceName, File destination)
  {
    try {
      runRsync(
        host+":"+path+File.separator+resourceName,
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
        host+":"+path+File.separator+destination,
        Arrays.asList("-aR", "--dirs", "--progress"));
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void putDirectory(File sourceDirectory, String destinationDirectory)
  {
    try {
      File remotePath = new File("target/rsync/"+path);
      remotePath.mkdirs();
      runRsync("target/rsync/", host+":/", Arrays.asList("-rd", "--progress"));
      runRsync(
        sourceDirectory.getCanonicalPath()+"/",
        host+":"+path+File.separator+destinationDirectory,
        Arrays.asList("-avc", "--progress"));
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

  private void setRepository(Repository repository)
  {
    this.repository = repository;

    String url = repository.getUrl();
    Pattern p = Pattern.compile("([^:]*):(([^/:]*):|)(.*)");
    Matcher m = p.matcher(url);
    if (!m.matches()) {
      System.err.println("Error: Couldn't decode url '"+url+"'");
    } else {
      protocol = m.group(1);
      host = m.group(3);
      path = m.group(4);
      System.out.println("Decoded url '"+url+"' to:");
      System.out.println(" Protocol '"+protocol+"'");
      System.out.println(" Host '"+host+"'");
      System.out.println(" Path '"+path+"'");
    }
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
    boolean processAlive = true;
    do {
      while (processOutput.ready()) {
        line = processOutput.readLine();
        if (line != null) getLog().info(line);
      }
      while (processErrors.ready()) {
        line = processErrors.readLine();
        if (line != null) getLog().error(line);
      }
      try {
        process.exitValue();
        processAlive = false;
      } catch (IllegalThreadStateException e) {
      }
    } while (processAlive);
    process.waitFor();
    } catch (Exception e) {
      getLog().error(e);
    }
}
